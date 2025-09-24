import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Git {
    public static void main(String[] args) {
        initializeRepo();
    }

    public static void initializeRepo() {
        File git = new File("git");
        File obj = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        if (git.exists() && obj.exists() && index.exists() && head.exists()) {
            System.out.println("Git Repository Already Exists");
        }
        else {
            if (!git.exists()) {
            git.mkdir();
        }
        if (!obj.exists()) {
            obj.mkdir();
        }
        try {
            index.createNewFile();   
            head.createNewFile();   
        }
        catch (Exception IOException) {
            System.out.println("Error: Git Repository Not Created");
        }
        System.out.println("Git Repository Created");
        }   
    }
}