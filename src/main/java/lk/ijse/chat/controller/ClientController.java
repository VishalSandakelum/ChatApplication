package lk.ijse.chat.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.KeyEvent;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public TextField typetxt;
    public Button sendbtn;
    public JFXTextArea textarea;
    public VBox messagContainer;
    public ScrollPane scrollPane;
    public Button emojibtn;
    public Button Camerabtn;
    public ScrollPane pane;

    static boolean openWindow = false;

    private static final double PANE_HEIGHT = 500;
    public Text nametxt;
    public Text namtext;
    public Text showNameBar;
    public static String name;

    Socket socket;

     DataInputStream dataInputStream;
     BufferedReader reader;
     DataOutputStream dataOutputStream;

     public String nam;
     public Text namset;
     public int count = 1;
     public String image;

    public ClientController() throws IOException {
    }

    public void sendbtnonAction(ActionEvent actionEvent) throws IOException {
        printName();
        new Thread(() -> {
        String reply;
            try {
                    reply = typetxt.getText();
                    if(reply!=null) {
                        dataOutputStream.writeUTF(reply);
                        dataOutputStream.flush();
                        appendMessageMe("Me :"+ reply, "-fx-border-color: #11D2E5; -fx-background-color: #34E9FA; -fx-background-radius: 20px 0px 20px 20px; -fx-border-radius: 20px 0px 20px 20px;");
                    }
                    typetxt.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namset.setText(ServerController.Allname.get(0));
        showNameBar.setText(ServerController.Allname.get(0));
        namtext.setText(ServerController.nam);
        closePane();
        new Thread(() -> {
        try {
            if(socket==null) {
                socket = new Socket("localhost", 3001);
            }
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            openWindow = true;
            System.out.println(ServerController.nam);

        } catch (IOException e) {

            e.printStackTrace();
        }

            String message;
            System.out.println(ServerController.Allname.size());
            while (true) {
                if(ServerController.Allname.size()>=1){
                    for(int i = count; i<ServerController.Allname.size(); i++){
                        namset.setText(namset.getText()+","+ServerController.Allname.get(i));
                        showNameBar.setText(namset.getText());
                        count++;
                    }
                }
                try {
                    message = dataInputStream.readUTF();
                    if(message!= null) {
                        image = message;
                            System.out.println("Server: " + message);
                            appendMessage(name +" :"+ message, "-fx-border-color: #CF76FF; -fx-background-color: #CF76FF; -fx-background-radius: 0px 20px 20px 20px; -fx-border-radius: 0px 20px 20px 20px;");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        try {
            Thread.sleep(500);
            pane.setVisible(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String message, String style) {
        if (message.matches(".*\\.(png|jpe?g|gif)$")) {
            Platform.runLater(() -> {
                try {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(105);
                    imageView.setPreserveRatio(true);

                    VBox imageContainer = new VBox(imageView);
                    imageContainer.setAlignment(Pos.CENTER_LEFT);
                    imageContainer.setPadding(new Insets(10));
                    imageContainer.setSpacing(10);

                    Label textLabel = new Label(name+" : ");
                    textLabel.setWrapText(true);
                    textLabel.setAlignment(Pos.CENTER_LEFT);
                    VBox.setMargin(textLabel, new Insets(0, 10, 0, 0));

                    HBox imageBox = new HBox(textLabel, imageContainer);
                    imageBox.setAlignment(Pos.CENTER_LEFT);
                    imageBox.setSpacing(10);

                    messagContainer.getChildren().add(imageBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else {
            Platform.runLater(() -> {
                Label messageLabel = new Label(message);
                messageLabel.setWrapText(true);
                messageLabel.setPadding(new Insets(10));
                messageLabel.setStyle(style);

                HBox messageContainer = new HBox(messageLabel);
                messageContainer.setAlignment(Pos.TOP_LEFT);
                messageContainer.setPadding(new Insets(10));
                messageContainer.setFillHeight(true);

                Label timeLabel = new Label(getCurrentTime());
                timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888888;");
                timeLabel.setPadding(new Insets(0, 0, 5, 0));
                HBox.setHgrow(timeLabel, Priority.ALWAYS);

                VBox chatBubble = new VBox(timeLabel, messageContainer);
                chatBubble.setAlignment(Pos.TOP_LEFT);
                chatBubble.setPadding(new Insets(5));

                messagContainer.getChildren().add(chatBubble);
            });
        }
    }
    private void appendMessageMe(String message, String style) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            //messageLabel.setPrefWidth(200);
            messageLabel.setPadding(new Insets(10));
            messageLabel.setStyle(style);

            messageLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);

            HBox messageContainer = new HBox(messageLabel);
            messageContainer.setAlignment(Pos.TOP_RIGHT);
            messageContainer.setPadding(new Insets(10));
            messageContainer.setFillHeight(true);

            Label timeLabel = new Label(getCurrentTime());
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888888;");
            timeLabel.setPadding(new Insets(0, 0, 5, 0));
            HBox.setHgrow(timeLabel, Priority.ALWAYS);

            VBox chatBubble = new VBox(timeLabel, messageContainer);
            chatBubble.setAlignment(Pos.TOP_RIGHT);
            chatBubble.setPadding(new Insets(5));

            messagContainer.getChildren().add(chatBubble);
        });
    }

    public void emojibtnonAction(ActionEvent actionEvent) throws AWTException {
        if(openWindow==true){
            openPane();
            typetxt.requestFocus();
            openWindow = false;
        }else {
            closePane();
            openWindow = true;
        }
    }

    private String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return currentTime.format(formatter);
    }

    public void CamerabtnonAction(ActionEvent actionEvent) {
        printName();
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory()+dialog.getFile();
        dialog.dispose();
        System.out.println(file + " chosen.");
        if (file != null) {
            sendFileToServer(file);
            displayFileInScrollPane(file);
        }
    }

    public void gringbigeyesOnAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f603"));
    }

    public void gringfacesmileonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f604"));
    }

    public void smillyonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f642"));
    }

    public void upsidedownonaction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f643"));
    }

    public void facewithtearsjoyonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f602"));
    }

    public void rollingfacewithtearsjoyonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f923"));
    }

    public void vinkifaceonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f609"));
    }

    public void savoringfoodonAction(ActionEvent actionEvent) {
        typetxt.appendText(convertEmojiCode("U+1f60B"));
    }

    private void openPane() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), pane);
        transition.setToY(0);
        transition.play();
    }

    private void closePane() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), pane);
        transition.setToY(PANE_HEIGHT);
        transition.play();
    }

    private String convertEmojiCode(String emojiCode) {
        int codePoint = Integer.parseInt(emojiCode.substring(2), 16);
        return new String(Character.toChars(codePoint));
    }

    private void sendFileToServer(String file) {
        try {
            dataOutputStream.writeUTF(file);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFileInScrollPane(String file) {
        Platform.runLater(() -> {
            try {
                ImageView imageView = new ImageView(file);
                imageView.setFitWidth(105);
                imageView.setPreserveRatio(true);

                VBox imageContainer = new VBox(imageView);
                imageContainer.setAlignment(Pos.CENTER_RIGHT);
                imageContainer.setPadding(new Insets(10));
                imageContainer.setSpacing(10);

                Label textLabel = new Label("Me : ");
                textLabel.setWrapText(true);
                textLabel.setAlignment(Pos.CENTER_RIGHT);
                VBox.setMargin(textLabel, new Insets(0, 10, 0, 0));

                HBox imageBox = new HBox(textLabel, imageContainer);
                imageBox.setAlignment(Pos.CENTER_RIGHT);
                imageBox.setSpacing(10);

                messagContainer.getChildren().add(imageBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void printName(){
        name = namtext.getText();
    }

}
