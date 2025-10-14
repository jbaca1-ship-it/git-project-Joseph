import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

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
            throw new IllegalArgumentException("createBLOB: said file does not exist.");
        }
        File dirCheck = new File(filePath);
        if (dirCheck.isDirectory()) {
            throw new IllegalArgumentException("createBLOB: cannot accept a directory as argument");
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

    public static void updateIndex(String fileName) {
        // please only call this on things that exist if you want things to work
        if (Files.exists(Paths.get("git/index"))) {
            String hash = hashFile(fileName);
            try {
                String contents = Files.readString(Paths.get("git/index"));
                if (!contents.isEmpty()) {
                    contents = contents + "\n";
                }
                File file = new File(fileName);
                Path basePath = Paths.get(new File("..").getAbsolutePath());
                Path relPath = Paths.get(file.getAbsolutePath());
                Path correctPath = basePath.relativize(relPath);
                String newThings = hash + " " + correctPath;
                // this deals with a file that's already in the index but has new content
                if (contents.contains(correctPath.toString()) && !contents.contains(hash)) {
                    ArrayList<String> lines = new ArrayList<String>(Files.readAllLines(Paths.get("git/index")));
                    for (int i = 0; i < lines.size(); i++) {
                        if (lines.get(i).contains(correctPath.toString())) {
                            lines.set(i, newThings);
                        }
                    }
                    StringBuilder str = new StringBuilder();
                    for (int i = 0; i < lines.size() - 1; i++) {
                        str.append(lines.get(i) + "\n");
                    }
                    str.append(lines.get(lines.size() - 1));
                    Files.write(Paths.get("git/index"), str.toString().getBytes(StandardCharsets.UTF_8));
                }
                //this gets anything that's not in there with the correct hash already
                else if (!contents.contains(correctPath.toString())) {
                    Files.write(Paths.get("git/index"), (contents + newThings).getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.err.println("Repository not initialized.\nHow exactly did this happen?\n\nlol");
        }
    }

    public static void robustReset() {
        // this will not work if there is no repo to reset
        File projectDir = new File(".");
        removeAllContents(projectDir);
        File index = new File("git/index");
        index.delete();
        try {
            index.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void removeAllContents(File dir) {
        // helper method for robustReset()
        File[] filesList = dir.listFiles();
        for (File f : filesList) {
            if (!f.getName().contains(".java") && f.getName().charAt(0) != '.' && !f.getName().equals("README.md")
                    && !f.getName().equals("index") && !f.getName().equals("HEAD")) {
                if (f.isDirectory()) {
                    removeAllContents(f);
                } else {
                    f.delete();
                }
            }
        }
    }
    
    public static String makeTree(String path) {
        StringBuilder contents = new StringBuilder();
        File dir = new File(path);
        if (!dir.exists()) {
            System.err.println("It's generally best to make trees of directories that exist... cough cough");
            return null;
        }
        else {
            File[] filesList = dir.listFiles();
        for (File f : filesList) {
            String fPath = f.getPath();
            if (!contents.isEmpty()) {
                if (f.isDirectory()) {
                makeTree(fPath);
                contents.append("\ntree " + makeTree(fPath) + " " + fPath);
            } else {
                createBLOB(fPath);
                contents.append("\nblob " + hashFile(fPath) + " " + fPath);
            }
            }
            else {
                if (f.isDirectory()) {
                makeTree(fPath);
                contents.append("tree " + makeTree(fPath) + " " + fPath);
            } else {
                createBLOB(fPath);
                contents.append("blob " + hashFile(fPath) + fPath);
            }
            }
        }
        String fileName = hashString(contents.toString());
        if (fileName == null) {
            System.err.println("Said file does not exist.");
        }
        if (!Files.exists(Paths.get(fileName))) {
            try {
                Files.createFile(Paths.get(fileName));
                Files.write(Paths.get(fileName), contents.toString().getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.err.println(e);
            }
            if (!Files.exists(Paths.get(path))) {
                System.err.println("Tree creation failed.");
            }
        }
        return hashString(contents.toString());
        }
    }

    private static String hashString(String str) {
        try {
            byte[] bytes = str.getBytes();
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
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e);
            return null;
        }
    }

    public static void createTreeFromIndex() {
        File wL = new File("workingList");
        // System.out.println(numSlashesTree("blob 7777777777777777777777777777777777777777 12/2/2/2/2.txt"));
        try {
            wL.createNewFile();
            byte[] content = Files.readAllBytes(Paths.get("git/index"));
            Files.write(Paths.get("workingList"), content);
            ArrayList<String> lines = new ArrayList<String>(Files.readAllLines(Paths.get("workingList")));
            ArrayList<GitObject> objects = new ArrayList<GitObject>();
            for (String line : lines) {
                objects.add(new GitObject(line.substring(0, 41), line.substring(41)));
            }
            // System.out.println(lines);
            Collections.sort(objects);
            // System.out.println(objects);
            StringBuilder wLCont = new StringBuilder();
            for (GitObject gitObject : objects) {
                wLCont.append("blob "+gitObject.toString() + "\n");
            }
            String data = wLCont.toString();
            Files.write(Paths.get("workingList"), data.trim().getBytes(StandardCharsets.UTF_8));
            String info = new String(Files.readAllBytes(Paths.get("workingList")));
            while (info.contains("\n")) {
                condense();
                info = new String(Files.readAllBytes(Paths.get("workingList")));
            }
            String rootHash = hashFile("workingList");
            File root = new File("git/objects/"+ rootHash);
            root.createNewFile();
            Files.write(Paths.get("git/objects/"+ rootHash), Files.readAllBytes(Paths.get("workingList")));
            Files.write(Paths.get("workingList"), ("tree " + rootHash + " (root)").getBytes(StandardCharsets.UTF_8));
            String topHash = hashFile("workingList");
            System.out.println(rootHash);
            System.out.println(topHash);
            File finalFile = new File("git/objects/"+topHash);
            finalFile.createNewFile();
            Files.write(Paths.get("git/objects/" + topHash), Files.readAllBytes(Paths.get("workingList")));
            Files.delete(Paths.get("workingList"));

        } catch (Exception e) {
        }
    }

    private static void condense() {
        try {
            ArrayList<String> lines = new ArrayList<String>(Files.readAllLines(Paths.get("workingList")));
            int mostSlashes = 0;
            for (int i = 0; i < lines.size(); i++) {
                if (numSlashesTree(lines.get(i)) > mostSlashes) {
                    mostSlashes = numSlashesTree(lines.get(i));
                }
            }
            ArrayList<String> longest = new ArrayList<String>();
            for (int i = 0; i < lines.size(); i++) {
                if (numSlashesTree(lines.get(i)) == mostSlashes) {
                    longest.add(lines.get(i));
                }
            }
            String path = "";
            for (int i = 0; i < longest.get(0).length(); i++) {
                if (longest.get(0).charAt(i) == '\\') {
                    path = longest.get(0).substring(46, i + 1);
                }
            }
            String contents = "";
            for (int i = 0; i < longest.size(); i++) {
                if (longest.get(i).contains(path)) {
                    contents += getFinalPath(longest.get(i)) + "\n";
                }
            }
            // System.out.println(longest);
            File newTree = new File("git/objects/"+hashString(contents.trim()));
            newTree.createNewFile();
            newTree.mkdir();
            Files.write(Paths.get("git/objects/"+hashString(contents.trim())), contents.trim().getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains(path)) {
                    lines.remove(i);
                    i--;
                }
            }
            lines.add("tree " + hashString(contents.trim()) +" "+ path.substring(0, path.length()-1));
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < lines.size(); i++) {
                data.append(lines.get(i)+"\n");
            }
            Files.write(Paths.get("workingList"), data.toString().trim().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
        }
    }
    
    private static int numSlashesTree(String str) {
        // System.out.println("help me");
        //   System.out.println("help me");
        str = str.substring(46);
        //  System.out.println("help me");
        int count = 0;
        //  System.out.println("help me");
        //  System.out.println("help me");
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') {
                count++;
            }
        }
        //  System.out.println("help me");
        return count;
    }
    
    private static String getFinalPath(String str) {
        int index = 0;
        String part1 = str.substring(0, 46);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') {
                index = i;
            }
        }
        return part1+str.substring(index + 1);
    }
}