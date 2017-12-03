package ku00015.shoutapp;

/**
 * Created by Klaidas on 25/11/2017.
 */
public class Comment {

    String cid;
    String pid;
    String uid;
    String comment;
    String user;

    public Comment(String cid, String pid, String uid, String comment, String user){
        this.cid = cid;
        this.pid = pid;
        this.uid = uid;
        this.comment = comment;
        this.user = user;
    }

    public String getCid() { return cid; }
    public String getPid() { return pid; }
    public String getUid() { return uid; }
    public String getComment() { return comment; }
    public String getUser() { return user; }

}
