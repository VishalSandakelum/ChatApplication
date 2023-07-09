package lk.ijse.chat.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.ijse.chat.client.ClientHandler;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    public TextField typetxt;
    public Button sendbtn;
    public JFXTextArea textarea;

     Socket socket;

     DataInputStream dataInputStream;
     BufferedReader reader;
     DataOutputStream dataOutputStream;
     ServerSocket serverSocket;
     private List<ClientHandler> clients = new ArrayList<>();
     public static ArrayList<String>Allname = new ArrayList<>();
     static String nam;

    public void sendbtnonAction(ActionEvent actionEvent) throws IOException {
        nam = typetxt.getText();
        Allname.add(nam);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Client.fxml"));
        Parent root1 = null;
        ClientController controller = new ClientController();
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxmlLoader.setController(controller);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Client");

        root1.setOnMouseClicked(event -> {
            System.out.println("Window clicked!");
        });

        stage.show();
        typetxt.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
        try {
            if(serverSocket==null) {
                serverSocket = new ServerSocket(3001);
            }
            while (!serverSocket.isClosed()){
                try{
                    socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket,clients);
                    clients.add(clientHandler);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        }).start();
    }


}
