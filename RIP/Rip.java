/*
Nomes:  Rafael Felipe Dias dos Santos
        Vinícius de Souza Carvalho RA: 726592
*/

import java.io.*;
import java.net.*;
import java.util.*;

class Rip implements Runnable {
    public Socket csocket;
    private static node nd;
    public static flag;

    public Rip (Socket connectionSocket){
        this.csocket = connectionSocket;
    }

    public static void main(String argv[]) throws Exception {

        flag = true;
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
        nd.getTable();
        new Thread(receive).start();
        new Thread(send).start();
    }

    private static Runnable receive = new Runnable() {
        public void run() {
            try{
                ServerSocket welcomeSocket = new ServerSocket(6520);

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
                    Socket clientSocket = new Socket("200.9.84.172", 6520+i);
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
                int[] sendVector;
                int i;

                while(true){
                    sendVector = nd.getTable();
                    StringBuilder sendMessage = new StringBuilder();
                    sendMessage = sendMessage.append(Integer.toString(nd.getId()) + '\n');
                    for(i = 0; i < 4; i++)
                        sendMessage = sendMessage.append(Integer.toString(sendVector[i]) + '\n');
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    if((inFromUser.readLine().equals("send") == true && flag == true){
                        if(nd.getId() == 0)
                            for(i = 0; i < 3; i++){
                                Socket clientSocket = new Socket("200.9.84.172", 6521+i);
                                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                outToServer.writeBytes(sendMessage.toString());
                                clientSocket.close();
                            }
                        else if(nd.getId() == 1) {
                            Socket clientSocket = new Socket("200.9.84.172", 6520);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                            Socket clientSocket = new Socket("200.9.84.172", 6522);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                        } else if(nd.getId() == 2) {
                            for(i = 0; i < 4; i++)
                                if(i != 2){
                                    Socket clientSocket = new Socket("200.9.84.172", 6520+i);
                                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                    outToServer.writeBytes(sendMessage.toString());
                                    clientSocket.close();
                                }
                        } else {
                            Socket clientSocket = new Socket("200.9.84.172", 6520);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                            Socket clientSocket = new Socket("200.9.84.172", 6522);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                        }
                    }
                }
            }

            catch(IOException a) {
                a.printStackTrace();
            }
        }
    };

}
