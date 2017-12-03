package ku00015.shoutapp;


public class Post {

    String pid;
    String uid;
    String title;
    String body;
    String user;
    String likes;

    /*
     * Allows for instantiating Post objects.
     */
    public Post(String p, String u, String t, String b, String user, String likes){
        this.pid = p;
        this.uid = u;
        this.title = t;
        this.body = b;
        this.user = user;
        this.likes = likes;
    }

    public String getPid() { return pid; }
    public String getUid() { return uid; }
    public String getPostTitle() { return title; }
    public String getBody() { return body; }
    public String getUser() { return user; }
    public String getLikes() { return likes; }

}
