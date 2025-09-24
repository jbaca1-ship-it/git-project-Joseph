Git.java

void initializeRepo()
This method initializes the Git repository. It creates the following files:
    -Directory "git" in the project root
    -Directory "objects" in "git"
    -File "index" in "git"
    -File "HEAD" in "git"
These files are only created if they do not currently exist.
In the case that everything necessary for initialization already exists, initializeRepo() will sysout a message stating that the repository already exists.


GitTester.java
The main method runs Git.initializeRepo(), verifyInstallation(), and cleanUp() ten times in a row. This shows that Git.initializeRepo() can run consistently.

void verifyInstallation()
This method checks to see if the files created by Git.initializeRepo() exists. It outputs a message stating whether or not the repo had been successfully initialized.

void cleanUp()
This method deletes all files initialized by Git.initializeRepo() and outputs a message that says that it has done so.