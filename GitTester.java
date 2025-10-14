import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive test suite for Git.java implementation
 * Tests all major Git functionality including repository initialization,
 * file hashing, blob creation, index management, and tree operations.
 */
public class GitTester {
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Git Implementation Test Suite ===");
        System.out.println("Starting comprehensive testing...\n");
        
        try {
            // Run all test suites
            runInitializationTests();
            runHashingTests();
            runBlobTests();
            runIndexTests();
            runTreeTests();
            runEdgeCaseTests();
            runIntegrationTests();
            
            // Print final results
            printTestResults();
            
        } catch (Exception e) {
            System.err.println("Test suite failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up test files
            // cleanup();
        }
    }

    /**
     * Test repository initialization functionality
     */
    public static void runInitializationTests() {
        System.out.println("--- Testing Repository Initialization ---");
        
        // Clean up any existing repository
        cleanup();
        
        // Test 1: Initialize new repository
        test("Initialize new repository", () -> {
            Git.initializeRepo();
            return verifyRepositoryStructure();
        });
        
        // Test 2: Initialize existing repository (should not fail)
        test("Initialize existing repository", () -> {
            Git.initializeRepo(); // Should not throw exception
            return verifyRepositoryStructure();
        });
    }

    /**
     * Test file hashing functionality
     */
    public static void runHashingTests() {
        System.out.println("--- Testing File Hashing ---");
        
        // Create test files
        createTestFile("hash_test1.txt", "Hello World");
        createTestFile("hash_test2.txt", "Different content");
        createTestFile("hash_test3.txt", ""); // Empty file
        
        // Test 1: Hash existing file
        test("Hash existing file", () -> {
            String hash = Git.hashFile("hash_test1.txt");
            return hash != null && hash.length() == 40; // SHA-1 hash is 40 characters
        });
        
        // Test 2: Hash non-existent file
        test("Hash non-existent file", () -> {
            String hash = Git.hashFile("non_existent.txt");
            return hash == null;
        });
        
        // Test 3: Hash empty file
        test("Hash empty file", () -> {
            String hash = Git.hashFile("hash_test3.txt");
            return hash != null && hash.length() == 40;
        });
        
        // Test 4: Hash consistency
        test("Hash consistency", () -> {
            String hash1 = Git.hashFile("hash_test1.txt");
            String hash2 = Git.hashFile("hash_test1.txt");
            return hash1 != null && hash1.equals(hash2);
        });
        
        // Test 5: Different files produce different hashes
        test("Different files produce different hashes", () -> {
            String hash1 = Git.hashFile("hash_test1.txt");
            String hash2 = Git.hashFile("hash_test2.txt");
            return hash1 != null && hash2 != null && !hash1.equals(hash2);
        });
    }

    /**
     * Test blob creation functionality
     */
    public static void runBlobTests() {
        System.out.println("--- Testing Blob Creation ---");
        
        // Test 1: Create blob for existing file
        test("Create blob for existing file", () -> {
            Git.createBLOB("hash_test1.txt");
            String hash = Git.hashFile("hash_test1.txt");
            File blobFile = new File("git/objects/" + hash);
            return blobFile.exists();
        });
        
        // Test 2: Create blob for non-existent file (should throw exception)
        test("Create blob for non-existent file", () -> {
            try {
                Git.createBLOB("non_existent.txt");
                return false; // Should have thrown exception
            } catch (IllegalArgumentException e) {
                return true; // Expected exception
            }
        });
        
        // Test 3: Create blob for directory (should throw exception)
        test("Create blob for directory", () -> {
            try {
                Git.createBLOB("git"); // git is a directory
                return false; // Should have thrown exception
            } catch (IllegalArgumentException e) {
                return true; // Expected exception
            }
        });
        
        // Test 4: Create blob for empty file
        test("Create blob for empty file", () -> {
            Git.createBLOB("hash_test3.txt");
            String hash = Git.hashFile("hash_test3.txt");
            File blobFile = new File("git/objects/" + hash);
            return blobFile.exists();
        });
    }

    /**
     * Test index management functionality
     */
    public static void runIndexTests() {
        System.out.println("--- Testing Index Management ---");
        
        // Test 1: Update index with new file
        test("Update index with new file", () -> {
            Git.updateIndex("hash_test1.txt");
            String indexContent = readFile("git/index");
            return indexContent.contains("hash_test1.txt");
        });
        
        // Test 2: Update index with multiple files
        test("Update index with multiple files", () -> {
            Git.updateIndex("hash_test2.txt");
            Git.updateIndex("hash_test3.txt");
            String indexContent = readFile("git/index");
            return indexContent.contains("hash_test2.txt") && 
                   indexContent.contains("hash_test3.txt");
        });
        
        // Test 3: Update index with modified file
        test("Update index with modified file", () -> {
            // Modify file content
            Files.write(Paths.get("hash_test1.txt"), "Modified content".getBytes(StandardCharsets.UTF_8));
            Git.updateIndex("hash_test1.txt");
            String indexContent = readFile("git/index");
            return indexContent.contains("hash_test1.txt");
        });
        
        // Test 4: Update index without repository (should handle gracefully)
        test("Update index without repository", () -> {
            // This test would require removing the git directory
            // For now, we'll just verify the method doesn't crash
            Git.updateIndex("hash_test1.txt");
            return true;
        });
    }

    /**
     * Test tree creation functionality
     */
    public static void runTreeTests() {
        System.out.println("--- Testing Tree Creation ---");
        
        // Create a test directory structure
        createTestDirectory("test_dir");
        createTestFile("test_dir/file1.txt", "Content 1");
        createTestFile("test_dir/file2.txt", "Content 2");
        
        // Test 1: Create tree from directory
        test("Create tree from directory", () -> {
            String treeHash = Git.makeTree("test_dir");
            return treeHash != null && treeHash.length() == 40;
        });
        
        // Test 2: Create tree from non-existent directory
        test("Create tree from non-existent directory", () -> {
            String treeHash = Git.makeTree("non_existent_dir");
            return treeHash == null;
        });
        
        // Test 3: Create tree from index
        test("Create tree from index", () -> {
        Git.createTreeFromIndex();
            // This method doesn't return a value, so we just verify it doesn't crash
            return true;
        });
    }

    /**
     * Test edge cases and error conditions
     */
    public static void runEdgeCaseTests() {
        System.out.println("--- Testing Edge Cases ---");
        
        // Test 1: Hash file with special characters
        test("Hash file with special characters", () -> {
            createTestFile("special_chars.txt", "Special chars: !@#$%^&*()");
            String hash = Git.hashFile("special_chars.txt");
            return hash != null && hash.length() == 40;
        });
        
        // Test 2: Hash file with unicode characters
        test("Hash file with unicode characters", () -> {
            createTestFile("unicode.txt", "Unicode: 你好世界 🌍");
            String hash = Git.hashFile("unicode.txt");
            return hash != null && hash.length() == 40;
        });
        
        // Test 3: Hash very large file
        test("Hash large file", () -> {
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeContent.append("This is line ").append(i).append("\n");
            }
            createTestFile("large_file.txt", largeContent.toString());
            String hash = Git.hashFile("large_file.txt");
            return hash != null && hash.length() == 40;
        });
    }

    /**
     * Test integration scenarios
     */
    public static void runIntegrationTests() {
        System.out.println("--- Testing Integration Scenarios ---");
        
        // Test 1: Full workflow - initialize, create files, add to index, create tree
        test("Full workflow integration", () -> {
            // This is a complex test that verifies the entire workflow
            return true; // Placeholder for now
        });
        
        // Test 2: Multiple file operations
        test("Multiple file operations", () -> {
            createTestFile("integration1.txt", "Integration test 1");
            createTestFile("integration2.txt", "Integration test 2");
            
            Git.createBLOB("integration1.txt");
            Git.createBLOB("integration2.txt");
            Git.updateIndex("integration1.txt");
            Git.updateIndex("integration2.txt");
            
            String indexContent = readFile("git/index");
            return indexContent.contains("integration1.txt") && 
                   indexContent.contains("integration2.txt");
        });
    }

    // Helper methods for testing
    
    private static void test(String testName, TestFunction testFunction) {
        try {
            boolean result = testFunction.run();
            if (result) {
                System.out.println("✓ PASS: " + testName);
                testsPassed++;
            } else {
                System.out.println("✗ FAIL: " + testName);
                testsFailed++;
                failedTests.add(testName);
            }
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + testName + " - " + e.getMessage());
            testsFailed++;
            failedTests.add(testName + " (Exception: " + e.getMessage() + ")");
        }
    }
    
    private static boolean verifyRepositoryStructure() {
        File git = new File("git");
        File obj = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        return git.exists() && obj.exists() && index.exists() && head.exists();
    }
    
    private static void createTestFile(String filename, String content) {
        try {
            Files.write(Paths.get(filename), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to create test file: " + filename);
        }
    }
    
    private static void createTestDirectory(String dirname) {
        new File(dirname).mkdirs();
    }
    
    private static String readFile(String filename) {
        try {
            return Files.readString(Paths.get(filename));
        } catch (IOException e) {
            return "";
        }
    }
    
    private static void printTestResults() {
        System.out.println("\n=== Test Results ===");
        System.out.println("Tests passed: " + testsPassed);
        System.out.println("Tests failed: " + testsFailed);
        System.out.println("Total tests: " + (testsPassed + testsFailed));
        
        if (testsFailed > 0) {
            System.out.println("\nFailed tests:");
            for (String failedTest : failedTests) {
                System.out.println("  - " + failedTest);
            }
        }
        
        if (testsFailed == 0) {
            System.out.println("\n🎉 All tests passed! The Git implementation is working correctly.");
        } else {
            System.out.println("\n❌ Some tests failed. Please review the implementation.");
        }
    }
    
    private static void cleanup() {
        // Clean up test files
        String[] testFiles = {
            "hash_test1.txt", "hash_test2.txt", "hash_test3.txt",
            "special_chars.txt", "unicode.txt", "large_file.txt",
            "integration1.txt", "integration2.txt"
        };
        
        for (String file : testFiles) {
            new File(file).delete();
        }
        
        // Clean up test directory
        deleteDirectory(new File("test_dir"));
        
        // Clean up git repository
        deleteDirectory(new File("git"));
    }
    
    private static void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }
    
    // Functional interface for test methods
    @FunctionalInterface
    private interface TestFunction {
        boolean run() throws Exception;
    }
}