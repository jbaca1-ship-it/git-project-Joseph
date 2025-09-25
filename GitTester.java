import java.io.File;

public class GitTester {
    public static void main(String[] args) {
        File testFile = new File("test.txt");
        try {
            testFile.createNewFile();
            } catch (Exception e) {
            System.err.println(e);
            }
            for (int i = 0; i < 10; i++) {
                Git.initializeRepo();
                verifyInitialization();
                Git.createBLOB("test.txt");
                cleanUp();
            }
        testFile.delete();
    }
    
    public static void verifyInitialization() {
        File git = new File("git");
        File obj = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        if (git.exists() && obj.exists() && index.exists() && head.exists()) {
            System.out.println("Repository initialization succeeded.");
        }
        else{
            System.out.println("Repository initialization failed.");
        }
    }
    
    public static void cleanUp() {
        File git = new File("git");
        File obj = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        File[] files = obj.listFiles();
        for (File file : files) {
            file.delete();
        }
        obj.delete();
        index.delete();
        head.delete();
        git.delete();
        System.out.println("Clean-up successful.");
    }
}
