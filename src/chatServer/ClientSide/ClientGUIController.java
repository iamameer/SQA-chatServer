package chatServer.ClientSide;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

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
    public Button btnSend = new Button(), btnStat = new Button(), btnList = new Button(), btnQuit = new Button();

    @FXML
    public TextField txtName = new TextField(), txtMessage = new TextField(), txtSend = new TextField();

    @FXML
    public TextArea txtChat = new TextArea();

    @FXML
    public RadioButton rbtnBroadcast = new RadioButton(), rbtnPM = new RadioButton();

    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String response = null;
    String action = null;

    private void initializeUI(){
        txtChat.setEditable(false);
        txtName.setDisable(false);
        txtSend.setDisable(true);
        txtMessage.setDisable(true);

        btnSend.setText("LOGIN");
        btnSend.setDisable(true);
        btnStat.setDisable(false);
        btnList.setDisable(false);
        btnQuit.setDisable(false);

        rbtnBroadcast.setSelected(true);
        rbtnBroadcast.setDisable(true);
        rbtnPM.setSelected(false);
        rbtnPM.setDisable(true);
    }

    private void setEvents(){

        txtName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtName.getText().isEmpty()){
                    txtMessage.setDisable(false);
                    btnSend.setDisable(false);
                    rbtnBroadcast.setDisable(false);
                    rbtnPM.setDisable(false);
                }else{
                    txtMessage.setDisable(true);
                    btnSend.setDisable(true);
                    rbtnBroadcast.setDisable(true);
                    rbtnPM.setDisable(true);
                }
            }
        });

        txtSend.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtSend.getText().isEmpty()){
                    btnSend.setDisable(false);
                }else {
                    btnSend.setDisable(true);
                }
            }
        });

        btnStat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action = "STAT";
                try{
                    out.println(action);
                    System.out.println("Sent: "+txtMessage.getText());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        btnList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action = "LIST";
                try{
                    out.println(action);
                    System.out.println("Sent: "+txtMessage.getText());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        btnQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action = "QUIT";
                try{
                    out.println(action);
                    System.out.println("Sent: "+txtMessage.getText());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        rbtnPM.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (rbtnPM.isSelected()){
                    txtSend.setDisable(false);
                    rbtnBroadcast.setSelected(false);
                    txtSend.setText("");
                    btnSend.setDisable(true);
                }else{
                    txtSend.setDisable(true);
                    rbtnBroadcast.setSelected(true);
                }
            }
        });

        rbtnBroadcast.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(rbtnBroadcast.isSelected()){
                    txtSend.setDisable(true);
                    rbtnPM.setSelected(false);
                }else{
                    txtSend.setDisable(false);
                    rbtnPM.setSelected(true);
                }
            }
        });

        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (btnSend.getText().equals("LOGIN")){
                    try{
                        action = "IDEN "+txtName.getText();
                        out.println(action);
                        System.out.println("Sent: "+action);
                    }catch (Exception e){e.printStackTrace();}
                    btnSend.setText("SEND");
                    txtName.setDisable(true);
                    txtMessage.setText("");
                }else if (btnSend.getText().equals("SEND")){
                    System.out.println("SEND");
                    if (rbtnBroadcast.isSelected() && !txtMessage.getText().isEmpty()){
                        action = "HAIL "+txtMessage.getText();
                    }else if (rbtnPM.isSelected() && !txtMessage.getText().isEmpty()){
                        action = "MESG "+txtSend.getText()+" "+txtMessage.getText();
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"No message entered!",ButtonType.OK);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.show();
                    }
                    try{
                        out.println(action);
                        System.out.println("Sent: "+action);
                    }catch (Exception e){e.printStackTrace();}
                    txtMessage.setText("");
                }
            }
        });
    }

    @Override
    public void initialize(URL location, final ResourceBundle resourceBundle){

        initializeUI();
        setEvents();

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

    }
}
