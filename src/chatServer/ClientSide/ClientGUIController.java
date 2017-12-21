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
import java.util.Timer;

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
        }catch (Exception e){e.printStackTrace();}

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("test");
                try{
                    response = in.readLine();
                    System.out.println("Receive: "+response);
                    if(!response.equals("")){txtChat.appendText(response + "\n");}
                }catch (Exception e){e.printStackTrace();}
            }
        },1000,1000);

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    out.println(txtMessage.getText());
                    System.out.println("Sent: "+txtMessage.getText());
                }catch (Exception e){e.printStackTrace();}
            }
        });

    }
}
