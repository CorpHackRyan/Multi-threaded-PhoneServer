package com.company;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.FileWriter;

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
            String received = "";
            String toreturn = "";
            String tokens[];
            String data_file_name = "phone_data.txt1";
            String get_tokens[];
            String name_found = "";

            while (true) {

                    try {

                    dos.writeUTF("\n**** PHONE SERVER ****\n-> STORE <name> <number> \t(where <number> = xxx-xxxx)\n" +
                            "-> GET <name>\n-> REMOVE <name>\n-> QUIT");


                    received = dis.readUTF();
                    tokens = received.split(" ");

                    if(received.equals("QUIT"))
                    {
                        System.out.println("Client " + this.s + " is disconnecting from server...");
                        this.s.close();
                        System.out.println("Connection closed to client: " + this.s);
                        break;
                    }

                    switch(tokens[0]) {

                        case "STORE":
                            FileWriter writer = new FileWriter(data_file_name, true);
                            writer.write(tokens[1] + " " + tokens[2] + "\n");
                            writer.close();
                            toreturn = "'" + tokens[1] + " " + tokens[2] + "' has been stored in local text file.";
                            dos.writeUTF(toreturn);
                            break;

                        case "GET":
                            File phone_data = new File(data_file_name);
                            Scanner reader = new Scanner(phone_data);

                            while (reader.hasNextLine()) {
                                String data = reader.nextLine();
                                get_tokens = data.split(" ");

                                // If line in text file matches client's request, send it back to client.
                                if (get_tokens[0].contains(tokens[1])) {
                                    toreturn = get_tokens[0] + " phone # is -> " + get_tokens[1];
                                    break;
                                }

                                toreturn = "No name was found with that request.";

                            } //

                            reader.close();
                            dos.writeUTF(toreturn);
                            break;

                        case "REMOVE":
                            toreturn = "REMOVE from SERVER";
                            dos.writeUTF(toreturn);
                            break;

                        default:
                            dos.writeUTF("Erroneous mess received. That is not a valid option. Try again.");
                            break;

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    //Scanner scanner = new Scanner(System.in);
                    //String err = scanner.nextLine();
                    System.out.println("print stack trade");
                }
            }
        }
    }
