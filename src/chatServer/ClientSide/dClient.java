package chatServer.ClientSide;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class dClient extends Application implements Initializable{

    @FXML
    public Button btnSend = new Button();

    @FXML
    public TextField txtName = new TextField(), txtMessage = new TextField();

    @FXML
    public TextArea txtChat = new TextArea();

    Socket myClient = null;
    BufferedReader input = null;
    PrintWriter out = null;
    String response = null;
    String oldresponse = null;
    static Runnable r;
    int i;

    private boolean canPrintFlag = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            myClient = new Socket("Alina", 9000);
            input = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
            out = new PrintWriter(myClient.getOutputStream(), true);
            response = input.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        txtChat.setEditable(false);

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canPrintFlag = true;
                out.println(txtMessage.getText());
                try {
                    response = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

       new Timer().scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               System.out.println("test");
               txtChat.appendText("test\n");
           }
       },1000,1000);



    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        }catch (Exception e){e.printStackTrace();}
    }
}