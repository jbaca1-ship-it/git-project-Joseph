Git.java

void initializeRepo()
This method initializes the Git repository. It creates the following files:
-Directory "git" in the project root
-Directory "objects" in "git"
-File "index" in "git"
-File "HEAD" in "git"
These files are only created if they do not currently exist.
In the case that everything necessary for initialization already exists, initializeRepo() will sysout a message stating that the repository already exists.

String hashFile(String filePath)
This method hashes the contents of the file associated with the path provided to it. It then returns the hashed value of these contents as a string. The SHA-1 hash from MessageDigest is employed to execute the hashing.

GitTester.java
The main method runs Git.initializeRepo(), verifyInstallation(), and cleanUp() ten times in a row. This shows that Git.initializeRepo() can run consistently.

void verifyInstallation()
This method checks to see if the files created by Git.initializeRepo() exists. It outputs a message stating whether or not the repo had been successfully initialized.

void cleanUp()
This method deletes all files initialized by Git.initializeRepo() and outputs a message that says that it has done so.
