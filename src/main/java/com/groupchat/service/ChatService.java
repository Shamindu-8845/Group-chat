package com.groupchat.service;

import com.groupchat.dto.Message;
import com.groupchat.socket.ClientHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatService {
    private final Map<String, ClientHandler> clients = new HashMap<>();

    public synchronized void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
        System.out.println("User connected: " + username);
    }

    public synchronized void removeClient(String username) {
        clients.remove(username);
        System.out.println("User disconnected: " + username);
    }

    public synchronized void broadcastMessage(Message message) {
        clients.values().forEach(client -> {
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                System.out.println("Error sending message to " + client);
                e.printStackTrace();
            }
        });
    }


}

