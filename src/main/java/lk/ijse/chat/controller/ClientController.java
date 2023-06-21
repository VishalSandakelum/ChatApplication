package lk.ijse.chat.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public TextField typetxt;
    public Button sendbtn;
    public JFXTextArea textarea;

     Socket socket;

     DataInputStream dataInputStream;
     BufferedReader reader;
     DataOutputStream dataOutputStream;

    public ClientController() throws IOException {
    }

    public void sendbtnonAction(ActionEvent actionEvent) throws IOException {
        new Thread(() -> {
        String reply;
            try {
                    reply = typetxt.getText();
                    if(reply!=null) {
                        dataOutputStream.writeUTF(reply);
                        dataOutputStream.flush();
                        textarea.appendText("\n"+"Me :"+reply);
                    }
                    typetxt.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
        try {
            if(socket==null) {
                socket = new Socket("localhost", 3001);
            }
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

            String message;
            while (true) {
                try {
                    message = dataInputStream.readUTF();
                    if(message!= null) {
                        System.out.println("Server: " + message);
                        textarea.appendText("\n"+"Server: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }



}
