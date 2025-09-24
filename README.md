Git.java

void initializeRepo()
This method initializes the Git repository. It creates the following files:
    -Directory "git" in the project root
    -Directory "objects" in "git"
    -File "index" in "git"
    -File "HEAD" in "git"
These files are only created if they do not currently exist.
In the case that everything necessary for initialization already exists, initializeRepo() will sysout a message stating that the repository already exists.