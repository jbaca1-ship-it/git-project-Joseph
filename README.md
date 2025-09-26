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

void createBlob(String fileName)
This method, given the String name of a file as a parameter, converts it into a BLOB which is stored in git/objects. The name of this BLOB is the hash of the file's contents. The hashing is performed with hashFile(String filePath). If the BLOB already exists, the method will do nothing. If the method has failed to create the BLOB, it will output a message that says so.

void updateIndex(String fileName)
This method, given the String name of a file as a parameter, adds its hash and name to the "index" file located within the "git" directory. It assumes that fileName accurately names an existing file. If no such file exists, it will update "index" with a null BLOB. If the repository has not been initialized, this method will return an error message and not run.

void robustReset()
This method calls the recursive function removeAllContents to delete anything in the project root that is not part of the git project or hidden.

void removeAllContents(File dir)
A method that recursively iterates through all directories in the project and deletes all files that are not part of the core project or hidden.

GitTester.java
The main method runs Git.initializeRepo(), verifyInstallation(), and cleanUp() ten times in a row. This shows that Git.initializeRepo() can run consistently.

void verifyInstallation()
This method checks to see if the files created by Git.initializeRepo() exists. It outputs a message stating whether or not the repo had been successfully initialized.

void cleanUp()
This method deletes all files initialized by Git.initializeRepo(), in addition to any BLOBS git/objects contains, and outputs a message that says that it has done so.

void comprehensiveTest()
This method runs a comprehensive test of features as of GP-2.4. It first cleans the root of any traces of the old repository. Then, it initializes a new repository and verifies that said repository has been created. Next, it creates five files with varying contents, BLOBs them, and updates the index accordingly. Lastly, it checks to see if this process has been run correctly. Please note that modifying the contents of any of these files will then require that the final testing of the index be changed to reflect the new tests.
