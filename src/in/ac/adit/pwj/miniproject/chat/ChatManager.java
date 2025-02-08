package in.ac.adit.pwj.miniproject.chat;

import java.io.*;
import java.util.*;

public class ChatManager {
    private Map<String, List<Message>> conversations;
    private Set<String> users;

    public ChatManager() {
        conversations = new HashMap<>();
        users = new HashSet<>();
    }

    public void addUser(String username) {
        users.add(username);
        conversations.put(username, new ArrayList<>());
    }

    public void removeUser(String username) {
        users.remove(username);
        conversations.remove(username);
        notifyUsers(username + " has left the chat.");
    }

    public void sendMessage(Message message) {
        conversations.get(message.sender).add(message);
        saveChatHistory(message.sender);

        if (message.isPrivate()) {
            sendPrivateMessage((TextMessage) message);
        } else {
            notifyUsers(message.getFormattedMessage());
        }
    }

    private void sendPrivateMessage(TextMessage message) {
        String recipient = message.getRecipient();
        if (conversations.containsKey(recipient)) {
            conversations.get(recipient).add(message);
            saveChatHistory(recipient);
        }
    }

    private void notifyUsers(String message) {
        System.out.println(message);
    }

    public List<Message> getChatHistory(String user) {
        return conversations.getOrDefault(user, new ArrayList<>());
    }

    private void saveChatHistory(String user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(user + "_chat.txt", true))) {
            for (Message msg : conversations.get(user)) {
                writer.write(msg.getFormattedMessage());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving chat history: " + e.getMessage());
        }
    }

    public Set<String> getConnectedUsers() {
        return users;
    }

    class MessageEncryptor {
        public String encrypt(String message) {
            return new StringBuilder(message).reverse().toString();
        }

        public String decrypt(String message) {
            return new StringBuilder(message).reverse().toString();
        }
    }
}
