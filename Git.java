import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

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
        if (testFile.exists()) {
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
        else {
            return null;
        }
    }
    
    public static void createBLOB(String filePath) {
        String fileName = hashFile(filePath);
        if (fileName == null) {
            System.err.println("Said file does not exist.");
        }
        String path = "git/objects/" + fileName;
        if (!Files.exists(Paths.get(path))) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(filePath)));
                Files.createFile(Paths.get("git/objects/" + fileName));
                Files.write(Paths.get("git/objects/" + fileName), data.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.err.println(e);
            }
            if (!Files.exists(Paths.get(path))) {
                System.err.println("BLOB creation failed.");
            }
        }
    }

    // private static void compressFile(String fileName) {
    //     try {
    //         FileInputStream fIn = new FileInputStream(fileName);
    //         FileOutputStream fOut = new FileOutputStream(".compress-" + fileName);
    //         DeflaterOutputStream dOut = new DeflaterOutputStream(fOut);
    //         int data;
    //         while ((data = fIn.read()) != -1) {
    //             dOut.write(data);
    //         }
    //         fIn.close();
    //         dOut.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // private static void decompressFile(String fileName) {
    //     try {
    //         FileInputStream fIn = new FileInputStream(fileName);
    //         FileOutputStream fOut = new FileOutputStream(fileName.substring(10));
    //         InflaterInputStream iIn = new InflaterInputStream(fIn);
    //         int data;
    //         while((data=iIn.read())!=-1)
    //         {
    //             fOut.write(data);
    //         }
    //         fOut.close();
    //         iIn.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public static void updateIndex(String fileName){
        if (Files.exists(Paths.get("git/index"))) {
            String hash = hashFile(fileName);
            try {
                String contents = Files.readString(Paths.get("git/index"));
                if (!contents.isEmpty()) {
                    contents += "\n";
                }
                Files.write(Paths.get("git/index"), (contents+hash+" "+fileName).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.err.println("Repository not initialized.\nHow exactly did this happen?\n\nlol");
        }
    }

    public static void robustReset() {
        File projectDir = new File(".");
        removeAllContents(projectDir);
    }
    
    private static void removeAllContents(File dir) {
        File[] filesList = dir.listFiles();
        for (File f : filesList) {
            if (!f.getName().contains(".java")&&f.getName().charAt(0)!='.'&&!f.getName().equals("README.md")) {
                if (f.isDirectory()) {
                    removeAllContents(f);
                }
                else {
                    f.delete();
                }
            }
        }
    }
}