package chatServer;

import chatServer.Connection;
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
            Assert.assertEquals(in.readLine().substring(0,29),"OK Welcome to the chat server");
        }catch (Exception e){e.printStackTrace();}
    }

    String action;
    @Test
    public void StatCommandTest(){
        try{
            client = new Socket("Alina",9000);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(),true);
        }catch (Exception e){e.printStackTrace();}


            action = "STAT";
            out.println(action);
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    String response;
                    try{
                        response = in.readLine();
                    }catch (Exception e){e.printStackTrace();}
                    Assert.assertEquals(response.substring(0,35),"There are currently");
                }
            }).start();


    }

}
