package chatServer.ClientSide;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ameer Sorne on 20/12/2017.
 */
public class GUItest extends Application {

    @FXML
    public Button btnSend = new Button();

    @FXML
    public TextField txtName = new TextField(), txtMessage = new TextField();

    @FXML
    public TextArea txtChat = new TextArea();

    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String response = null;
    String oldresponse = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (Exception e){e.printStackTrace();}

        try{
            socket = new Socket("Alina", 9000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            response = in.readLine();
            System.out.println(response);
            txtChat.appendText(response+"\n");
            oldresponse = response;
        }catch (Exception e){e.printStackTrace();}

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    out.println(txtMessage.getText());
                    System.out.println("Sent: "+txtMessage.getText());
                    if (in.ready()){
                        response = in.readLine();
                        System.out.println("Receive: "+response);
                    }
                    txtChat.appendText(response + "\n");
                    response = oldresponse;
                }catch (Exception e){e.printStackTrace();}
            }
        });

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    if (!oldresponse.equals(response)){
                        System.out.println("Receive: "+response);
                        txtChat.appendText(response + "\n");
                    }
                }catch (Exception e) {e.printStackTrace();}
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
