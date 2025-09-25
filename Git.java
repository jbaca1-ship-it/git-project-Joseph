import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {
    public static void main(String[] args) {
        initializeRepo();
        createBLOB("nukes.txt");
    }

    public static void initializeRepo() {
        File git = new File("git");
        File obj = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        if (git.exists() && obj.exists() && index.exists() && head.exists()) {
            System.out.println("Git Repository Already Exists");
        } else {
            if (!git.exists()) {
                git.mkdir();
            }
            if (!obj.exists()) {
                obj.mkdir();
            }
            try {
                index.createNewFile();
                head.createNewFile();
            } catch (Exception IOException) {
                System.out.println("Error: Git Repository Not Created");
            }
            System.out.println("Git Repository Created");
        }
    }
    
    public static String hashFile(String filePath) {
        File testFile = new File(filePath);
        if (!testFile.exists()) {
            throw new IllegalArgumentException("No such file exists.");
        }
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            String content = new String(bytes, StandardCharsets.UTF_8);
            if (!content.isEmpty() && content.charAt(0) == '\uFEFF') {
                content = content.substring(1);
            }
            content = content.replace("\r\n", "\n").replace("\r", "\n");
            byte[] normalizedBytes = content.getBytes(StandardCharsets.UTF_8);
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = mDigest.digest(normalizedBytes);
            StringBuilder hashedString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hexB = Integer.toHexString(0xff & b);
                if (hexB.length() == 1) {
                    hashedString.append("0");
                }
                hashedString.append(hexB);
            }
            return hashedString.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e);
            return null;
        }
    }
    
    public static void createBLOB(String filePath) {
        String fileName = hashFile(filePath);
        String path = "git/objects/" + fileName;
        if (!Files.exists(Paths.get(path))) {
            try {
            String data = new String(Files.readAllBytes(Paths.get(filePath)));
            Files.createFile(Paths.get("git/objects/" + fileName));
            Files.write(Paths.get("git/objects/"+fileName), data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e);
        }
        if (!Files.exists(Paths.get(path))) {
            System.err.println("BLOB creation failed.");
        }
        }
    }
}