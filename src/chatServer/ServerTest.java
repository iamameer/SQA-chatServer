package chatServer;

import chatServer.ClientSide.ClientGUI;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

        Assert.assertEquals(9000,client.getLocalPort());
    }


  /*  @Test
    public void testConnection(){


        Assert.assertEquals(9000,client.getPort());
        Assert.assertTrue(client.isConnected());
    }*/

}
