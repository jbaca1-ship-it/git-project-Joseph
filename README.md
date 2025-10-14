# Git Implementation in Java

A Java implementation of core Git functionality including repository initialization, file hashing, blob creation, index management, and tree operations.

## Overview

This project provides a simplified but functional implementation of Git's core features in Java. It includes repository management, file versioning, and basic tree operations that mirror the behavior of the real Git version control system.

## Features

- **Repository Initialization**: Create and manage Git repositories
- **File Hashing**: SHA-1 hashing for content identification
- **Blob Management**: Store file contents as Git objects
- **Index Management**: Track staged files and changes
- **Tree Operations**: Create hierarchical directory structures
- **Comprehensive Testing**: Full test suite with edge case coverage

## Project Structure

```
├── Git.java          # Core Git implementation
├── GitObject.java    # Helper class for tree operations
├── GitTester.java    # Comprehensive test suite
└── README.md         # This documentation
```

## Quick Start

### 1. Initialize a Repository

```java
// Create a new Git repository
Git.initializeRepo();
```

This creates the following structure:

- `git/` - Repository root directory
- `git/objects/` - Object storage directory
- `git/index` - Staging area file
- `git/HEAD` - Current branch reference

### 2. Hash Files

```java
// Hash a file's contents
String hash = Git.hashFile("example.txt");
System.out.println("File hash: " + hash);
```

### 3. Create Blobs

```java
// Store file as Git blob
Git.createBLOB("example.txt");
```

### 4. Update Index

```java
// Stage files for commit
Git.updateIndex("example.txt");
Git.updateIndex("another-file.txt");
```

### 5. Create Trees

```java
// Create tree from directory
String treeHash = Git.makeTree("src/");

// Create tree from index
Git.createTreeFromIndex();
```

## API Reference

### Git Class

#### `void initializeRepo()`

Initializes a new Git repository with the standard directory structure.

**Example:**

```java
Git.initializeRepo();
```

#### `String hashFile(String filePath)`

Computes the SHA-1 hash of a file's contents.

**Parameters:**

- `filePath` - Path to the file to hash

**Returns:** SHA-1 hash as a 40-character hexadecimal string, or `null` if file doesn't exist

**Example:**

```java
String hash = Git.hashFile("document.txt");
```

#### `void createBLOB(String fileName)`

Creates a Git blob object from a file and stores it in the objects directory.

**Parameters:**

- `fileName` - Path to the file to convert to blob

**Throws:** `IllegalArgumentException` if file doesn't exist or is a directory

**Example:**

```java
Git.createBLOB("source.java");
```

#### `void updateIndex(String fileName)`

Adds a file to the Git index (staging area).

**Parameters:**

- `fileName` - Path to the file to stage

**Example:**

```java
Git.updateIndex("modified-file.txt");
```

#### `String makeTree(String path)`

Creates a tree object from a directory structure.

**Parameters:**

- `path` - Path to the directory to convert to tree

**Returns:** Hash of the created tree object

**Example:**

```java
String treeHash = Git.makeTree("src/");
```

#### `void createTreeFromIndex()`

Creates a tree object from the current index contents.

**Example:**

```java
Git.createTreeFromIndex();
```

#### `void robustReset()`

Removes all non-Git files from the project directory.

**Example:**

```java
Git.robustReset();
```

### GitObject Class

Helper class used internally for tree operations.

**Fields:**

- `hash` - Object hash
- `path` - File path

**Methods:**

- `compareTo(GitObject other)` - Compares objects by path
- `toString()` - Returns formatted string representation

## Testing

The project includes a comprehensive test suite in `GitTester.java` that covers:

- Repository initialization
- File hashing functionality
- Blob creation and management
- Index operations
- Tree creation
- Edge cases and error conditions
- Integration scenarios

### Running Tests

```bash
javac *.java
java GitTester
```

The test suite will:

1. Run all test categories
2. Display pass/fail results for each test
3. Provide a summary of test results
4. Clean up test files automatically

### Test Categories

- **Initialization Tests**: Verify repository creation
- **Hashing Tests**: Test file hashing with various content types
- **Blob Tests**: Test blob creation and error handling
- **Index Tests**: Test staging area functionality
- **Tree Tests**: Test tree creation and management
- **Edge Case Tests**: Test special characters, unicode, large files
- **Integration Tests**: Test complete workflows

## Error Handling

The implementation includes robust error handling:

- **File Not Found**: Methods return `null` or throw appropriate exceptions
- **Invalid Operations**: Clear error messages for invalid operations
- **Directory vs File**: Proper validation for blob creation
- **Repository State**: Validation of repository initialization

## Implementation Details

### Hashing Algorithm

- Uses SHA-1 for content hashing
- Normalizes line endings (CRLF → LF)
- Handles UTF-8 encoding properly
- Removes BOM (Byte Order Mark) if present

### Object Storage

- Blobs stored in `git/objects/` directory
- Filename is the SHA-1 hash of content
- Supports both files and directory trees

### Index Format

- Each line contains: `hash path`
- Automatically updates when files change
- Maintains relative paths from project root

## Limitations

- No compression (blobs stored as plain text)
- No network operations
- No branch management
- No merge operations
- Limited to basic Git operations

## Contributing

When contributing to this project:

1. Ensure all tests pass
2. Add tests for new functionality
3. Follow existing code style
4. Update documentation as needed

## License

This project is for educational purposes and demonstrates Git internals implementation in Java.
