/* ClientGUI.java : MAIN class to load the GUI FXML
*
*  Methods        : main()  - Main Method
*                   start() - connecting FXML file and launching scene
* */

package chatServer.ClientSide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ClientGUI extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //connecting FXML GUI file
        try{
            Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        }catch (Exception e){e.printStackTrace();}
    }
}
