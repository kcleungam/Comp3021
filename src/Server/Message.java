package Server;

/**
 * Created by Krauser on 7/5/2016.
 */
public class Message {
    public int fronID;
    public int toID;
    public String content;

    public Message( int fronID, int toID, String content){
        this.fronID = fronID;
        this.toID = toID;
        this.content = content;
    }
}
