package chatServer.ClientSide;/**
 * Created by Ameer Sorne on 18/12/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (Exception e){e.printStackTrace();}


    }
}
