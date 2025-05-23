package com.groupchat;

import com.groupchat.config.AppConfig;
import com.groupchat.service.ChatService;
import com.groupchat.socket.ChatServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Thread(() -> {
            ChatService chatService = new ChatService();
            ChatServer server = new ChatServer(AppConfig.SERVER_PORT, chatService);
            server.start();
        }).start();

        showLoginWindow();
    }
    //loginView form load here
    public static void showLoginWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(AppInitializer.class.getResource("/view/LoginView.fxml"));
        Parent root = loader.load();

        Stage loginStage = new Stage();
        loginStage.setTitle("Group Chat Login");
        loginStage.setScene(new Scene(root));
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}