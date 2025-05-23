module Group.Chat {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.groupchat.controller to javafx.fxml;
    exports com.groupchat;
}