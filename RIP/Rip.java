/*
Nomes:  Rafael Felipe Dias dos Santos
        Vinícius de Souza Carvalho RA: 726592
*/

import java.io.*;
import java.net.*;
import java.util.*;

class Multicast implements Runnable {
    public Socket csocket;
    private node nd;

    public Multicast (Socket connectionSocket, int c){
        this.csocket = connectionSocket;
    }

    public static void main(String argv[]) throws Exception {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(inFromUser.readLine());
        int[][] inTable = new int[4][2];
        for(int i = 0; i < 4; i++){
            inTable[i][0] = Integer.parseInt(inFromUser.readLine());
            if(inTable[i][0] != 999)
                inTable[i][1] = i;
            else
                inTable[i][1] = -1;
        }
        nd = new node(inTable, id);

        new Thread(receive).start();
        //new Thread(send).start();
    }

    private static Runnable receive = new Runnable() {
        public void run() {
            try{
                ServerSocket welcomeSocket = new ServerSocket(/*Porta entre 6520 e 5523 para três processos*/);

            while(true) {
                Socket connectionSocket = welcomeSocket.accept();

                Multicast s = new Multicast(connectionSocket);
                Thread t = new Thread(s);
                t.start();
            }
        }
            catch(IOException a) {
                a.printStackTrace();
            }
        }
    };

    public void run(){
        try{
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.csocket.getInputStream()));
            int id = Integer.parseInt(inFromClient.readLine());
            int[] rcvdTable = new int[4];
            for(int i = 0; i < 4; i++)
                rcvdTable[i] = Integer.parseInt(inFromClient.readLine());
            if(nd.rtUpdate(rcvdTable, id) == true){

                for(i = 0; i < 3; i++){
                    Socket clientSocket = new Socket(/*"IP"*/, 6520+i);
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    outToServer.writeBytes(ackMessage.toString());
                    clientSocket.close();
                }
            }
        }
        catch(IOException a) {
                a.printStackTrace();
        }
    }

  private static Runnable send = new Runnable() {
        public void run() {
            try{
                int i;

                while(true){
                    StringBuilder sendMessage = new StringBuilder();
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    String message = inFromUser.readLine();
                    sendMessage = sendMessage.append("0" + '\n' + Integer.toString(clock) + '\n' + Integer.toString(pid) + '\n' + message + '\n');
                    for(i = 0; i < 3; i++){
                        Socket clientSocket = new Socket(/*"IP"*/, 6520+i);
                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        outToServer.writeBytes(sendMessage.toString());
                        clientSocket.close();
                    }
                }
            }

            catch(IOException a) {
                a.printStackTrace();
            }
        }
    };

}
