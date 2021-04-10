// This is not my code. It was give to use to build a client model based off this server.
// I have modified it a little to suit my needs.

package com.company;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class PhoneServer {

    public static void main(String[] args) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


        /**
        * Open a server socket on the specified port number (2014) and monitor the port
        * for connection requests. When a connection request is received, create a client
        * request thread, passing to its constructor a reference to the Socket object that
        * represents the established connection with the client.
        */

        System.out.println("Server listening");

        // The server socket.
        int my_port = 2014;
        ServerSocket listener = new ServerSocket(my_port);

        // Begin infinite loop for server to wait for client connection requests
        while (true)
        {

            Socket clientSocket = null;

            try {

                // Handle incoming client request
                clientSocket = listener.accept();

                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now) + "-> Client has connected to Multi Threaded PhoneServer 1.0 from: " + clientSocket);

                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                Thread thread = new ClientThread(clientSocket, dis, dos);
                thread.start();

            }
            catch (Exception e) {
                clientSocket.close();
                e.printStackTrace();

                }


        }

        //System.out.println("Client connected from: " + clientSocket);



        // new java.util.Scanner(System.in).nextLine();


    }
}

class ClientThread extends Thread
{
    private final DataInputStream dis;
    private final DataOutputStream dos;
    Socket s;

    // Constructor
    public ClientThread(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;

    }

    @Override
    public void run()
    {
        String received;
        String toreturn;

        while(true) {
            try {
                dos.writeUTF("What do you want");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }   // handleConnection(socket);
    }
}