package com.company;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;


public class PhoneServer {

    public static void main(String[] args) throws Exception {

        System.out.println("Server is running..");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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
    }
}


class ClientThread extends Thread
{
    private final DataInputStream dis;
    private final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientThread(Socket s, DataInputStream dis, DataOutputStream dos) {

        this.s = s;
        this.dis = dis;
        this.dos = dos;

    }
    
        @Override
        public void run()
        {
            // Wait for user input

            String user_name;
            String received;
            String toreturn;
            boolean name_pass = false;

            while (true) {
                try {

                    Scanner scanner = new Scanner(System.in);

                    if (name_pass)
                    {
                        continue;

                    } else {
                        user_name = dis.readUTF();
                        name_pass = true;

                    }

                    System.out.println("Connected to: " + user_name);


                    String err = scanner.nextLine();

                    dos.writeUTF("What do you want to do\n-> STORE <name> <number> \twhere <number> = xxx-xxxx\n" +
                            "-> GET <name>\n-> REMOVE <name>\n-> QUIT");


                    received = dis.readUTF();


                } catch (IOException e) {
                    // e.printStackTrace();
                    //Scanner scanner = new Scanner(System.in);
                    //String err = scanner.nextLine();
                    System.out.println("print stack trade");
                }
            }
        }
    }
