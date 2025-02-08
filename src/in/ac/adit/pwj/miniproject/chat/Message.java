package in.ac.adit.pwj.miniproject.chat;

import java.util.Date;

public class Message {
    protected String sender;
    protected long timestamp;

    public Message(String sender) {
        this.sender = sender;
        this.timestamp = System.currentTimeMillis();
    }

    public String getFormattedMessage() {
        return "[" + new Date(timestamp) + "] " + sender + ": ";
    }

    public boolean isPrivate() {
        return false;
    }
}
