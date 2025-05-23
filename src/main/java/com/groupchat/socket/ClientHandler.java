package com.groupchat.socket;

import com.groupchat.dto.Message;
import com.groupchat.service.ChatService;
import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatService chatService;
    private ObjectOutputStream outputStream;
    private String username;

    public ClientHandler(Socket socket, ChatService chatService) {
        this.clientSocket = socket;
        this.chatService = chatService;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            
            Message loginMessage = (Message) inputStream.readObject();
            this.username = loginMessage.getSender();
            chatService.broadcastMessage(new Message("Server", 
                username + " has joined the chat!", Message.MessageType.NOTIFICATION));
            
            chatService.addClient(username, this);
            
            while (true) {
                Message message = (Message) inputStream.readObject();
                if(message.getType()==Message.MessageType.TEXT){
                    chatService.broadcastMessage(message);
                }
                if (message.getType()==Message.MessageType.NOTIFICATION){
                    chatService.broadcastMessage(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected: " + username);
        } finally {
            chatService.removeClient(username);
            if (username != null) {
                chatService.broadcastMessage(new Message("Server", 
                    username + " has left the chat!", Message.MessageType.NOTIFICATION));
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }
}