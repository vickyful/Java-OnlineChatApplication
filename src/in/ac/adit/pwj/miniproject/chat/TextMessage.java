package in.ac.adit.pwj.miniproject.chat;

public class TextMessage extends Message {
    private String content;
    private String recipient;

    public TextMessage(String sender, String content) {
        super(sender);
        this.content = content;
    }

    public TextMessage(String sender, String content, String recipient) {
        super(sender);
        this.content = content;
        this.recipient = recipient;
    }

    @Override
    public String getFormattedMessage() {
        String msg = super.getFormattedMessage() + content;
        if (recipient != null) {
            msg += " (private to " + recipient + ")";
        }
        return msg;
    }

    @Override
    public boolean isPrivate() {
        return recipient != null;
    }

    public String getRecipient() {
        return recipient;
    }
}
