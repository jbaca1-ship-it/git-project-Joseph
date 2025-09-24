import java.io.File;

public class GitTester {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Git.initializeRepo();                
            verifyInitialization();
            cleanUp();
       }
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
        git.delete();
        obj.delete();
        index.delete();
        head.delete();
        System.out.println("Clean-up successful.");
    }
}
