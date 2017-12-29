package chatServer.Testing;

import chatServer.ClientSide.ClientGUI;
import chatServer.Server;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GUITest extends ApplicationTest{

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.show();
    }

    public <T extends Node> T search(final String text){
        return lookup(text).query();
    }

    @Before
    public void beforeTest() throws Exception{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server(9000);
            }
        });
        thread.start();
        ApplicationTest.launch(ClientGUI.class);
    }

    @After
    public void afterTest() throws Exception{
        //TODO
    }

    @Test
    public void loginTest() {
        Button btnSend = GuiTest.find("#btnSend");

        clickOn("#txtName");
        write("TESTER");
        assertThat(btnSend.getText(), is("LOGIN"));
        assertThat(btnSend.isDisabled(), is(false));
    }


}
