package lk.ijse.chat.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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

    public void sendbtnonAction(ActionEvent actionEvent) throws IOException {
        new Thread(() -> {
        String message="";
        String reply;
        while (!message.equals("finish")) {
            try {
                message = dataInputStream.readUTF();
                System.out.println("Client: " + message);
                System.out.print("Server: ");
                reply = reader.readLine();
                dataOutputStream.writeUTF(reply);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
        try {
            serverSocket = new ServerSocket(3001);
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        }).start();
    }
}
