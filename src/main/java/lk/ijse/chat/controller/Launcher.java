package lk.ijse.chat.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        openWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Client.fxml"))));
        stage.setTitle("Client");
        stage.centerOnScreen();
        stage.show();
    }

    public void openWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Server.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
        stage.setTitle("server");
    }
}
