/* ClientGUIController.java : Controller class for the UI
*
*  Methods        : initializeUI()  - Initializing UI at initial states
*                   setEvents()     - Setting up listeners(events) to the items
*                   initialize()    - Launching as soon as a controller is called(when a UI created)
* */

package chatServer.ClientSide;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.*;
import java.util.Timer;

public class ClientGUIController implements Initializable{

    //FXML binding
    @FXML
    public Button btnSend = new Button(), btnStat = new Button(), btnList = new Button(), btnQuit = new Button();
    @FXML
    public TextField txtName = new TextField(), txtMessage = new TextField(), txtSend = new TextField();
    @FXML
    public TextArea txtChat = new TextArea();
    @FXML
    public RadioButton rbtnBroadcast = new RadioButton(), rbtnPM = new RadioButton();

    //Global variables
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String response = null;
    String action = null;

    //Method to initialize UI at initial states
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

    //Method to set up listeners(events) to the items
    private void setEvents(){
        //Listener for textName, that will enable certain UI once text is typed into it
        txtName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtName.getText().isEmpty()){
                    btnSend.setDisable(false);
                }else{
                    txtMessage.setDisable(true);
                    btnSend.setDisable(true);
                    rbtnBroadcast.setDisable(true);
                    rbtnPM.setDisable(true);
                    btnSend.setText("LOGIN");
                }
            }
        });

        //Listener for textSend, that enable button Send if the text is not empty
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

        //Event for button STATUS
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

        //Event for button LIST
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

        //Event for button QUIT
        btnQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action = "QUIT";
                try{
                    out.println(action);
                    System.out.println("Sent: "+txtMessage.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"OK Goodbye",ButtonType.OK);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.show();
                    System.exit(0);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Radiobutton Listener, change of item properties
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

        //Radiobutton Listener, change of item properties
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

        //Event for button SEND
        //This button has 2 FUNCTIONs, which depends on the text displayed
        //LOGIN: it will register the user into server via IDEN command
        //SEND: it will send deliver message to the server
        //    - Under SEND it will check whether its a broadcast(HAIL) or private message(MESG)
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
                    txtMessage.setDisable(false);
                    rbtnBroadcast.setDisable(false);
                    rbtnPM.setDisable(false);
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
                //Bad response
                if (    response.equals("BAD invalid command to server") ||
                        response.equals("BAD command not recognised") ||
                        response.equals("BAD Your message is badly formatted") ||
                        response.equals("BAD the user does not exist")){
                    txtMessage.clear();
                    txtMessage.setDisable(false);
                }else if(response.equals("BAD username is already taken") ||
                         response.equals("BAD You have not logged in yet") ||
                         response.substring(0,18).equals("BAD you are already")){
                    txtName.clear();
                    txtName.setDisable(false);
                }
            }
        });
    }

    //Method launching as soon as a controller is called(when a UI created)
    @Override
    public void initialize(URL location, final ResourceBundle resourceBundle){

        //Setting up UI and events
        initializeUI();
        setEvents();

        //Creating a new connection with the server
        try{
            socket = new Socket("Alina", 9000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        }catch (Exception e){e.printStackTrace();}

        //A thread to keep on listening replies from the server
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("test");
                try{
                    response = in.readLine();
                    System.out.println("Receive: "+response);
                    if(!response.isEmpty()){txtChat.appendText(response + "\n");}
                }
                catch (SocketException se){se.printStackTrace(); System.exit(0);}
                catch(Exception e){e.printStackTrace();}
            }
        },1000,1000);
    }
}
