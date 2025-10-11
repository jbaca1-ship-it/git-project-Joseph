public class GitObject implements Comparable<GitObject> {
    // private String type;
    private String hash;
    private String path;

    public GitObject(String hash, String path) {
        // this.type = type;
        this.hash = hash;
        this.path = path;
    }

    @Override
    public int compareTo(GitObject o) {
        if (path.compareTo(o.path) < 0) {
            return -1;
        } else if (path.compareTo(o.path) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
    

    // /**
    //  * @return the type
    //  */
    // public String getType() {
    //     return type;
    // // }

    // /**
    //  * @param type the type to set
    //  */
    // public void setType(String type) {
    //     this.type = type;
    // }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    public String toString() {
        return hash + " " + path;
    }
}