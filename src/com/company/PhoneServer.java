// This is not my code. It was give to use to build a client model based off this server.
// I have modified it a little to suit my needs.

package com.company;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;


public class PhoneServer {

    public static void main(String[] args) throws Exception {

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



        while (true) {

            Socket clientSocket = null;

            try {

                // Handle incoming client request
                clientSocket = listener.accept();
                System.out.println("Client has connected to PhoneServer 1.0 from: " + clientSocket);

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
