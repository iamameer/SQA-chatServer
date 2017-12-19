package chatServer.ClientSide;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.security.Principal;
import java.util.*;

/**
 * Created by Ameer Sorne on 18/12/2017.
 */
public class ClientGUIController implements Initializable{

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
    public void initialize(URL location, final ResourceBundle resourceBundle){
        try{
            socket = new Socket("Alina", 9000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            response = in.readLine();
            System.out.println(response);
            txtChat.appendText(response+"\n");
            oldresponse = response;
        }catch (Exception e){e.printStackTrace();}

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

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                        out.println(txtMessage.getText());
                        System.out.println("Sent: "+txtMessage.getText());
                        response = in.readLine();
                        System.out.println("Receive: "+response);
                        txtChat.appendText(response + "\n");
                        response = oldresponse;
                    //SwingUtilities.invokeLater(r);
                }catch (Exception e){e.printStackTrace();}
            }
        });

    }
}
