import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitTester {
    public static void main(String[] args) {
        // File testFile = new File("test.txt");
        // try {
        //     testFile.createNewFile();
        //     } catch (Exception e) {
        //     System.err.println(e);
        //     }
        //     for (int i = 0; i < 10; i++) {
        //         Git.initializeRepo();
        //         verifyInitialization();
        //         Git.createBLOB("test.txt");
        //         cleanUp();
        //     }
        // testFile.delete();
        cleanUp();
        Git.initializeRepo();
        // File testFile = new File("test2.txt");
        // try {
            // testFile.createNewFile();
            // Files.write(Paths.get("test2.txt"), "There is an imposter among us too".getBytes(StandardCharsets.UTF_8));
        // } catch (Exception e) {
        //     System.err.println(e);
        // }
        Git.updateIndex("test.txt");
        Git.updateIndex("test2.txt");
        Git.updateIndex("test3.txt");
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
