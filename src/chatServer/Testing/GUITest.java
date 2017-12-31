package chatServer.Testing;

import chatServer.ClientSide.ClientGUI;
import chatServer.Server;
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GUITest extends ApplicationTest{

    //Global Variables
    final String welcomeMessage = "OK Welcome to the chat server";
    final String STATreply = "OK There are currently";
    final String LISTreplyNotRegistered = "BAD You have not logged in yet";
    final String LISTreplyRegistered = "OK TESTER, ";
    final String IDENreply = "OK Welcome to the chat server ";
    final String HAILreply = "Broadcast from";
    final String MESGreply = "OK your message has been sent";
    final String QUITreplyNotRegistered = "OK goodbye";

    final String username = "TESTER";
    final String username2 = "TESTER2";
    final String testMessage = "This is a test message";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.show();
    }

    //Method to invoke BEFORE starting a test
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

    //Method to invoke AFTER starting a test
    @After
    public void afterTest() throws Exception{
        //TODO
    }

    //TestCase 1.1 - Initial State
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

    //TestCase 1.2 - Connected to Server
    @Test
    public void TestCase1_2_ConnectedToServer() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");
        //Awaiting reply from server
        Thread.sleep(1000);
        assertThat(txtChat.getText().contains(welcomeMessage), is(true));
    }

    //TestCase 2.1 - State Listener = txtName
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

    //TestCase 2.2 - State Listener = txtSend
    @Test
    public void TestCase2_2_StateListener_txtSend() throws Exception{
        Button btnSend = GuiTest.find("#btnSend");
        TextField txtSend = GuiTest.find("#txtSend");
        RadioButton rbtnPM = GuiTest.find("#rbtnPM");

        //Logging in to enable certain items
        TestCase4_LoginTest();
        //Enabling Private Message mode
        rbtnPM.setSelected(true);

        //A: txtSend write
        clickOn("#txtSend");
        write(username2);
        assertThat(btnSend.isDisabled(), is(false));

        //B: txtSend cleared
        Thread.sleep(2000);
        txtSend.clear();
        assertThat(btnSend.isDisabled(), is(true));
    }

    //TestCase 2.3 - State Listener = rbtnPM & rbtnBroadcast
    @Test
    public void TestCase2_3_StateListener_rbtnPM_rbtnBroadcast() throws Exception{
        RadioButton rbtnBroadcast = GuiTest.find("#rbtnBroadcast");
        RadioButton rbtnPM = GuiTest.find("#rbtnPM");
        TextField txtSend = GuiTest.find("#txtSend");
        Button btnSend = GuiTest.find("#btnSend");

        //Logging in to enable certain items
        TestCase4_LoginTest();

        //A: rbtnPM selected
        rbtnPM.setSelected(true);
        assertThat(txtSend.isDisabled(), is(false));
        assertThat(txtSend.getText(), is(""));
        assertThat(btnSend.isDisabled(), is(true));
        assertThat(rbtnBroadcast.isSelected(), is(false));

        //B: rbtnBroadcast Selected
        Thread.sleep(2000);
        rbtnBroadcast.setSelected(true);
        assertThat(txtSend.isDisabled(), is(true));
        assertThat(rbtnPM.isSelected(), is(false));
        assertThat(btnSend.isDisabled(), is(false));
    }

    //TestCase 3.1 - Event Listener = btnStat
    @Test
    public void TestCase3_1_EventListener_btnStat() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");

        clickOn("#btnStat");

        //Awaiting reply from server
        Thread.sleep(3000);
        assertThat(txtChat.getText().contains(STATreply), is(true));
    }

    //TestCase 3.2 - Event Listener = btnList
    @Test
    public void TestCase3_2_EventListener_btnList() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");

        //A: Not-registered
        clickOn("#btnList");

        //Awaiting reply from server
        Thread.sleep(3000);
        assertThat(txtChat.getText().contains(LISTreplyNotRegistered), is(true));

        //B: Registered
        TestCase4_LoginTest();
        Thread.sleep(1000);
        clickOn("#btnList");

        //Awaiting reply from server
        Thread.sleep(3000);
        assertThat(txtChat.getText().contains(LISTreplyRegistered), is(true));
    }

    //TestCase 3.3 - Event Listener = btnQuit (Not registered)
    @Test
    public void TestCase3_3_EventListener_btnQuit() throws Exception {
        TextArea txtChat = GuiTest.find("#txtChat");

        //A: Not-registered
        clickOn("#btnQuit");

        //Awaiting reply from server
        Thread.sleep(3000);
        assertThat(txtChat.getText().contains(QUITreplyNotRegistered), is(true));
    }

    //TestCase 3.4 - Event Listener = btnSend
    @Test
    public void TestCase3_4_EventListener_btnSend() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");

        //A: LOGIN
        TestCase4_LoginTest();

        //B1: SEND Broadcast
        clickOn("#txtMessage");
        write(testMessage);
        clickOn("#btnSend");

        //Awaiting reply from server
        Thread.sleep(2000);
        assertThat(txtChat.getText().contains(HAILreply), is(true));

        //B2: SEND PM
        Thread.sleep(10000); //Wait 10seconds to manually launch 2nd GUI
        clickOn("#rbtnPM");
        clickOn("#txtSend");
        write(username2);
        clickOn("#txtMessage");
        write(testMessage);
        clickOn("#btnSend");

        //Awaiting reply from server
        Thread.sleep(2000);
        assertThat(txtChat.getText().contains(MESGreply), is(true));
    }

    //TestCase 4 - Login Test
    @Test
    public void TestCase4_LoginTest() throws Exception{
        TextArea txtChat = GuiTest.find("#txtChat");

        clickOn("#txtName");
        write(username);
        clickOn("#btnSend");

        Thread.sleep(2000);
        assertThat(txtChat.getText().contains(IDENreply), is(true));
    }
}
