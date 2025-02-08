package in.ac.adit.pwj.miniproject.chat;

public class FileMessage extends Message {
    private String fileName;
    private byte[] fileData;
    private String recipient;

    public FileMessage(String sender, String fileName, byte[] fileData) {
        super(sender);
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public FileMessage(String sender, String fileName, byte[] fileData, String recipient) {
        super(sender);
        this.fileName = fileName;
        this.fileData = fileData;
        this.recipient = recipient;
    }

    @Override
    public String getFormattedMessage() {
        String msg = super.getFormattedMessage() + "File sent: " + fileName;
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
