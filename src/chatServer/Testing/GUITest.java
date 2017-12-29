package chatServer.Testing;

import chatServer.ClientSide.ClientGUI;
import chatServer.Server;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GUITest extends ApplicationTest{

    //Global Variables
    final String welcomeMessage = "OK Welcome to the chat server";
    final String STATreply = "OK There are currently";
    final String LISTreply = "";
    final String IDENreply = "OK Welcome to the chat server ";
    final String HAILreply = "Broadcast from";
    final String MESGreply = "OK your message has been sent";
    final String QUITreplyNotRegistered = "OK goodbye";
    final String QUITreplyRegistered = "OK thank you for sending";

    final String username = "TESTER";
    final String testMessage = "This is a test message";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.show();
    }

    public <T extends Node> T search(final String text){
        return lookup(text).query();
    }

    @Before
    public void beforeTest() throws Exception{
        //Starting a server thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server(9000);
            }
        });
        thread.start();

        //Launching GUI
        ApplicationTest.launch(ClientGUI.class);
    }

    @After
    public void afterTest() throws Exception{
        //TODO
    }

    @Test
    public void TestCase1_1_InitialState(){
        //Declaring variables
        Button btnSend = GuiTest.find("#btnSend");
        Button btnStat = GuiTest.find("#btnStat");
        Button btnList = GuiTest.find("#btnList");
        Button btnQuit = GuiTest.find("#btnQuit");

        TextField txtName = GuiTest.find("#txtName");
        TextField txtMessage = GuiTest.find("#txtMessage");
        TextField txtSend = GuiTest.find("#txtSend");

        TextArea txtChat = GuiTest.find("#txtChat");

        RadioButton rbtnBroadcast = GuiTest.find("#rbtnBroadcast");
        RadioButton rbtnPM = GuiTest.find("#rbtnPM");

        assertThat(txtChat.isEditable(), is(false));
        assertThat(txtName.isDisabled(), is(false));
        assertThat(txtSend.isDisabled(), is(true));
        assertThat(txtMessage.isDisabled(), is(true));

        assertThat(btnSend.getText(), is("LOGIN"));
        assertThat(btnSend.isDisabled(), is(true));
        assertThat(btnStat.isDisabled(), is(false));
        assertThat(btnList.isDisabled(), is(false));
        assertThat(btnQuit.isDisabled(), is(false));

        assertThat(rbtnBroadcast.isSelected(), is(true));
        assertThat(rbtnBroadcast.isDisabled(), is(true));
        assertThat(rbtnPM.isSelected(), is(false));
        assertThat(rbtnPM.isDisabled(), is(true));
    }

    @Test
    public void TestCase1_2_ConnectedToServer() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");
        Thread.sleep(1000);
        assertThat(txtChat.getText().contains(welcomeMessage), is(true));
    }


    @Test
    public void TestCase2_1_StateListener_txtName() throws Exception{
        Button btnSend = GuiTest.find("#btnSend");
        TextField txtName = GuiTest.find("#txtName");

        //A: txtName write
        clickOn("#txtName");
        write(username);
        assertThat(btnSend.getText(), is("LOGIN"));
        assertThat(btnSend.isDisabled(), is(false));

        //B: txtName cleared
        Thread.sleep(2000);
        txtName.clear();
        assertThat(btnSend.getText(), is("LOGIN"));
        assertThat(btnSend.isDisabled(), is(true));
    }

    @Test
    public void TestCase2_2_StateListener_txtSend() throws Exception{
        Button btnSend = GuiTest.find("#btnSend");
        TextField txtSend = GuiTest.find("#txtSend");

        //A: txtSend write
        clickOn("#txtSend");
        write(testMessage);
        assertThat(btnSend.isDisabled(), is(false));

        //B: txtSend cleared
        Thread.sleep(2000);
        txtSend.clear();
        assertThat(btnSend.isDisabled(), is(true));
    }

    //TODO: LOGIN FIRST
    @Test
    public void TestCase2_3_StateListener_rbtnPM_rbtnBroadcast() throws Exception{
        RadioButton rbtnBroadcast = GuiTest.find("#rbtnBroadcast");
        RadioButton rbtnPM = GuiTest.find("#rbtnPM");
        TextField txtSend = GuiTest.find("#txtSend");
        Button btnSend = GuiTest.find("#btnSend");

        //Login here

        //A: rbtnPM selected
        rbtnPM.setSelected(true);
        assertThat(txtSend.isDisabled(), is(false));
        assertThat(txtSend.getText(), is(""));
        assertThat(btnSend.isDisabled(), is(true));
        assertThat(rbtnBroadcast.isSelected(), is(false));

        //B: rbtnBroadcast Selected
        Thread.sleep(3000);
        rbtnBroadcast.setSelected(true);
        assertThat(txtSend.isDisabled(), is(true));
        assertThat(rbtnPM.isSelected(), is(false));
        assertThat(btnSend.isDisabled(), is(false));
    }

    @Test
    public void TestCase3_1_EventListener_btnStat(){

    }

    @Test
    public void TestCase3_2_EventListener_btnList(){

    }

    @Test
    public void TestCase3_3_EventListener_btnQuit(){

    }

    @Test
    public void TestCase3_4_EventListener_btnSend(){

        //A: LOGIN

        //B1: SEND Broadcast

        //B2: SEND PM

    }

}
