package com.groupchat.controller;

import com.groupchat.config.AppConfig;
import com.groupchat.dto.Message;
import com.groupchat.socket.ChatClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;


import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class ChatController {
    @FXML private TextArea messageArea;
    @FXML private ListView<String> userList;
    @FXML private VBox chatContainer;
    @FXML private Button sendButton;

    private ChatClient chatClient;
    private String username;

    public void setUsername(String username) {
        this.username = username;
        initializeClient();
    }

    private void initializeClient() {
        chatClient = new ChatClient();
        try {
            chatClient.connect(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT, username);
            chatClient.setMessageHandler(this::handleIncomingMessage);
        } catch (IOException e) {
            showAlert("Connection Error", "Could not connect to server");
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSendMessage() {
        String text = messageArea.getText().trim();
        if (!text.isEmpty()) {
            try {
                Message message = new Message(username, text, Message.MessageType.TEXT);
                chatClient.sendMessage(message);
                messageArea.clear();
            } catch (IOException e) {
                showAlert("Error", "Could not send message");
            }
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            event.consume();
            handleSendMessage();
        }
    }

    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            switch (message.getType()) {
                default:
                    addTextMessage(message);
            }
        });
    }

    private void addTextMessage(Message message) {
        String formattedMessage = formatMessage(message);
        Label messageLabel = new Label(formattedMessage);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-padding: 5px; -fx-background-color: #e6e6e6; -fx-background-radius: 5px;");

        HBox messageBox = new HBox(messageLabel);
        if (message.getSender().equals(username)) {
            messageBox.setStyle("-fx-alignment: center-right; -fx-padding: 5px;");
            System.out.println(formattedMessage);
        } else {
            messageBox.setStyle("-fx-alignment: center-left; -fx-padding: 5px;");
        }

        chatContainer.getChildren().add(messageBox);

    }

    //here create the format of the massage
    private String formatMessage(Message message) {
        String timestamp = message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"));

        switch (message.getType()) {
            case TEXT:
                return String.format("[%s] %s: %s", timestamp, message.getSender(), message.getContent());
            case NOTIFICATION:
                return String.format("[%s] %s", timestamp, message.getContent());
            default:
                return message.getContent();
        }
    }

    //Alert create here
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}