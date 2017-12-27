package chatServer;

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
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(),true);
        }catch (Exception e){e.printStackTrace();}

        Assert.assertTrue(client.isConnected());
        Assert.assertEquals(9000,client.getPort());
    }

    BufferedReader in;
    PrintWriter out;
    @Test
    public void serverReplyTest(){
       setupClientTest();

        try{
            Assert.assertEquals("OK Welcome to the chat server",in.readLine().substring(0,29));
        }catch (Exception e){e.printStackTrace();}
    }

    String action,response;
    boolean start = true;
    @Test
    public void STATcommandTest(){
        setupClientTest();
        try{
            //reading out the welcome message
            response = in.readLine();
        }catch (Exception e){e.printStackTrace();}

        while (start){
            try{
                //Sending out STAT command
                action = "STAT";
                out.println(action);
                response = in.readLine().substring(0,22);
                Assert.assertEquals("OK There are currently",response);
                System.out.println(response);
                if (response!=null){start = false;}
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Test
    public void LISTcommandTest(){
        setupClientTest();
        try{
            //reading out the welcome message
            response = in.readLine();
        }catch (Exception e){e.printStackTrace();}

        while (start){
            try{
                IDENcommandTest();
                //Sending out STAT command
                action = "LIST";
                out.println(action);
                response = in.readLine().substring(0,3);
                Assert.assertEquals("OK ",response);
                System.out.println(response);
                if (response!=null){start = false;}
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Test
    public void IDENcommandTest(){
        setupClientTest();
        try{
            //reading out the welcome message
            response = in.readLine();
        }catch (Exception e){e.printStackTrace();}

        while (start){
            try{
                //assuming a username
                String username = "TESTER";
                //Sending out IDEN command to server
                action = "IDEN "+username;
                out.println(action);
                //Cutting out empty response from server
                while(in.readLine()==null){
                    response = in.readLine();
                }
                //Moving on to the next line from server
                response = in.readLine();
                Assert.assertEquals("OK Welcome to the chat server "+username,response);
                System.out.println(response);
                if (response!=null){start = false;}
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Test
    public void HAILcommandTest(){
        setupClientTest();
        try{
            //reading out the welcome message
            response = in.readLine();
        }catch (Exception e){e.printStackTrace();}

        while (start){
            try{
                IDENcommandTest();
                //Sending out STAT command
                action = "HAIL test broadcast";
                out.println(action);
                response = in.readLine().substring(0,14);
                Assert.assertEquals("Broadcast from",response);
                System.out.println(response);
                if (response!=null){start = false;}
            }catch (Exception e){e.printStackTrace();}
        }
    }
}
