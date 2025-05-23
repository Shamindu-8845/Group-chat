package com.groupchat.controller;

import com.groupchat.dao.UserDao;
import com.groupchat.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    private final UserService userService = new UserService(new UserDao());

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        
        if (userService.authenticate(username, password)) {
            openChatWindow(username);
        } else {
            showAlert("Error", "Invalid username or password");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        
        if (userService.registerUser(username, password)) {
            showAlert("Alert", "User registered successfully");
        } else {
            showAlert("Error", "Username already exists");
        }

    }

    private void openChatWindow(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChatView.fxml"));
            Parent root = loader.load();

            ChatController chatController = loader.getController();
            chatController.setUsername(username);

            Stage chatStage = new Stage();
            chatStage.setTitle("Group Chat - " + username);
            chatStage.setScene(new Scene(root));
            chatStage.show();

            passwordField.clear();
            usernameField.clear();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open chat window");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}