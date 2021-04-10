package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class PhoneClient {

    public static void main(String[] args) throws Exception {
        /**
         * Open a server socket on the specified port number (2014) and monitor the port
         * for connection requests. When a connection request is received, create a client
         * request thread, passing to its constructor a reference to the Socket object that
         * represents the established connection with the client.
         */

        System.out.println("Client run");

        int    client_port = 2015;
        int    server_port = 2014;
        String server_ip   = "localhost";

        //Establish a connection with the Phone Server
        Socket connection_to_PServer = new Socket(server_ip, server_port);

        System.out.println("Please enter your name: ");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();


        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(connection_to_PServer.getInputStream());
        DataOutputStream dos = new DataOutputStream(connection_to_PServer.getOutputStream());

        dos.writeUTF(msg);

        while (true)
        {
            System.out.println(dis.readUTF());
            String tosend = scanner.nextLine();
            dos.writeUTF(tosend);

            if(tosend.equals("Exit"))
            {
                System.out.println("Closing this connection : " + connection_to_PServer);
                connection_to_PServer.close();
                System.out.println("Connection closed");
                break;

            }
        }

        }

    }

