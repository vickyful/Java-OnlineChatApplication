package in.ac.adit.pwj.miniproject.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<String, PrintWriter> userWriters = new HashMap<>();
    private static PrintWriter fileWriter;
    private static final Map<String, String> emojiMap = new HashMap<>(); // Emoji mapping

    public static void main(String[] args) {
        emojiMap.put(":smile:", "\uD83D\uDE03");
        emojiMap.put(":sad:", "\uD83D\uDE1E");
        emojiMap.put(":thumbsup:", "\uD83D\uDC4D");
        emojiMap.put(":heart:", "\uD83D\uDC9C");

        System.out.println("Chat server started...");
        try {
            fileWriter = new PrintWriter(new FileWriter("chat_history.txt", true), true);
            ServerSocket serverSocket = new ServerSocket(12346);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                username = in.readLine();
                synchronized (clientWriters) {
                    clientWriters.add(out);
                    userWriters.put(username, out);
                }

                String joinMessage = username + " has joined the chat.";
                System.out.println(joinMessage);
                updateUserStatus(); 

                out.println("Welcome to the chat, " + username + "!");

                String message;
                while ((message = in.readLine()) != null) {
                    message = replaceEmojiCodes(message); 
                    String formattedMessage = username + ": " + message;
                    System.out.println(formattedMessage); 
                    broadcast(formattedMessage); 
                    logMessage(formattedMessage); 
                    }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                    userWriters.remove(username);
                }

            
                String leaveMessage = username + " has left the chat.";
                System.out.println(leaveMessage);
                updateUserStatus();
            }
        }

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }

        private void logMessage(String message) {
            fileWriter.println(message);
        }

        private void updateUserStatus() {
            StringBuilder statusMessage = new StringBuilder("Online users: ");
            for (String user : userWriters.keySet()) {
                statusMessage.append(user).append(", ");
            }
            if (statusMessage.length() > 2) {
                statusMessage.setLength(statusMessage.length() - 2);
            }
            broadcast(statusMessage.toString());
        }

        private String replaceEmojiCodes(String message) {
            for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }
    }
}
