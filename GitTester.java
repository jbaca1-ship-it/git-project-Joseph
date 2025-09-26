import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitTester {
    public static void main(String[] args) {
        comprehensiveTest();
        Git.robustReset();
        cleanUp();
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

    public static void comprehensiveTest() {
        File file1 = new File("test1.txt");
        File file2 = new File("test2.txt");
        File file3 = new File("test3.txt");
        File file4 = new File("test4.txt");
        File file5 = new File("test5.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
            file4.createNewFile();
            file5.createNewFile();
            Files.write(Paths.get("test1.txt"), "Text".getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get("test2.txt"), "SIGMAROBERTS1000".getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get("test3.txt"), "Cooper Ren's SSN (REAL): 568-82-6423".getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get("test4.txt"), "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e);
        }
        Git.initializeRepo();
        cleanUp();
        Git.initializeRepo();
        verifyInitialization();
        Git.createBLOB("test1.txt");
        Git.createBLOB("test2.txt");
        Git.createBLOB("test3.txt");
        Git.createBLOB("test4.txt");
        Git.createBLOB("test5.txt");
        Git.updateIndex("test1.txt");
        Git.updateIndex("test2.txt");
        Git.updateIndex("test3.txt");
        Git.updateIndex("test4.txt");
        Git.updateIndex("test5.txt");
        try {
            String indexContents = Files.readString(Paths.get("git/index"));
            String correctContents = "c3328c39b0e29f78e9ff45db674248b1d245887d test1.txt\n6673313176a748c8aa9b975a10db63aa333dae91 test2.txt\nf0e450555c24daaa256a8415005c577879f6b741 test3.txt\n8bfb89b8cc3b5fe3085c0fc92a65907bb3a5e170 test4.txt\nda39a3ee5e6b4b0d3255bfef95601890afd80709 test5.txt";
            if (!indexContents.equals(correctContents)) {
                System.out.println("ERROR!\nExpected:\n" + correctContents + "\nActual:\n" + indexContents);
            }
            else {
                System.out.println("The code works like a champ. Bravo.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
