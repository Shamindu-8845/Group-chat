package com.groupchat.socket;

import com.groupchat.dto.Message;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Consumer<Message> messageHandler;

    public void connect(String host, int port, String username) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        
        sendMessage(new Message(username, "", Message.MessageType.NOTIFICATION));
        
        new Thread(this::listenForMessages).start();
    }

    public void sendMessage(Message message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }

    public void setMessageHandler(Consumer<Message> messageHandler) {
        this.messageHandler = messageHandler;
    }

    private void listenForMessages() {
        try {
            while (true) {
                Message message = (Message) inputStream.readObject();
                if (messageHandler != null) {
                    messageHandler.accept(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected from server");
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}