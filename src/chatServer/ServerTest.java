package chatServer;

import chatServer.Connection;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerTest {

    ServerSocket server;
    @Test
    public void setupServerTest(){
       try{
           server = new ServerSocket(9000);
       }catch (Exception e){e.printStackTrace();}

       Assert.assertEquals(9000,server.getLocalPort());
    }

    Socket client;
    @Test
    public void setupClientTest(){
        try{
            client = new Socket("Alina",9000);
        }catch (Exception e){e.printStackTrace();}

        Assert.assertTrue(client.isConnected());
        Assert.assertEquals(9000,client.getPort());
    }

    BufferedReader in;
    PrintWriter out;
    @Test
    public void serverReplyTest(){
       try{
           client = new Socket("Alina",9000);
           in = new BufferedReader(new InputStreamReader(client.getInputStream()));
           out = new PrintWriter(client.getOutputStream(),true);
       }catch (Exception e){e.printStackTrace();}

        try{
            Assert.assertEquals("OK Welcome to the chat server",in.readLine().substring(0,29));
        }catch (Exception e){e.printStackTrace();}
    }

    String action,response;
    boolean start = true;
    @Test
    public void STATCommandTest(){
        try{
            client = new Socket("Alina",9000);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(),true);
            //reading out the welcome message
            response = in.readLine();
        }catch (Exception e){e.printStackTrace();}

        while (start){
            try{
                //Sending out STAT command
                action = "STAT";
                out.println(action);
                //response = in.readLine().substring(0,22);
                Assert.assertEquals("OK There are currently",in.readLine().substring(0,22));
                System.out.println(response);
                if (response!=null){start = false;}
            }catch (Exception e){e.printStackTrace();}
        }
        /*new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    response = in.readLine().substring(0,18);
                    Assert.assertEquals(response,"There are currentl");
                    System.out.println(response);
                }catch (Exception e){e.printStackTrace();}
            }
        },1000,1000);*/
    }

}
