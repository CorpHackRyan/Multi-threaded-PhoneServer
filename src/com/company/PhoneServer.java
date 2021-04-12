package com.company;
import java.io.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.FileWriter;

public class PhoneServer {

    public static void main(String[] args) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        int my_port = 2014;
        ServerSocket listener = new ServerSocket(my_port);

        System.out.println("Server is running..");

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
            String data_file_name = "phone_data.txt";
            String tmp_data_file_name = "phone_data.tmp";

            String received = "";
            String toreturn = "";
            String name_deleted = "";

            String tokens[];
            String get_tokens[];

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
                            }

                            reader.close();
                            dos.writeUTF(toreturn);
                            break;

                        case "REMOVE":
                            FileWriter writer_tmp = new FileWriter(tmp_data_file_name);
                            File phone_data_tmp = new File(data_file_name);
                            Scanner reader_tmp = new Scanner(phone_data_tmp);

                            while (reader_tmp.hasNextLine()) {
                                String line_read = reader_tmp.nextLine();
                                get_tokens = line_read.split(" ");

                                if (get_tokens[0].contains(tokens[1])) {
                                    // dont write to the new temp file
                                    name_deleted = get_tokens[0] + " was deleted from the database.";
                                    continue;

                                } else {

                                    writer_tmp.write(get_tokens[0] + " " + get_tokens[1] + "\n");
                                    System.out.println("Writing to temp file " + get_tokens[0] + " " + get_tokens[1]);
                                }
                            }

                            writer_tmp.close();
                            reader_tmp.close();

                            // Delete original file & replace with new file
                            File original_file = new File(data_file_name);
                            original_file.delete();

                            File oldName = new File(tmp_data_file_name);
                            File newName = new File(data_file_name);
                            oldName.renameTo(newName);

                            dos.writeUTF(name_deleted);
                            break;

                        default:
                            dos.writeUTF("Erroneous message received. That is not a valid option. Please try again.");
                            break;

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
