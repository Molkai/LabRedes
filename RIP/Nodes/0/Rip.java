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
    public static boolean flag;

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
        System.out.printf("Inicialmente a tabela do node %d é:\n", nd.getId());
        nd.printTable();
        System.out.printf("\n");
        new Thread(receive).start();
        new Thread(send).start();
    }

    private static Runnable receive = new Runnable() {
        public void run() {
            try{
                ServerSocket welcomeSocket = new ServerSocket(6520);

                while(true) {
                    Socket connectionSocket = welcomeSocket.accept();

                    Rip s = new Rip(connectionSocket);
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
            boolean aux;

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.csocket.getInputStream()));
            int id = Integer.parseInt(inFromClient.readLine());
            int[] rcvdTable = new int[4];
            for(int i = 0; i < 4; i++)
                rcvdTable[i] = Integer.parseInt(inFromClient.readLine());
            System.out.printf("Recebido Vetor do node %d\n", id);
            aux = nd.rtUpdate(rcvdTable, id);
            if(flag == false)
                flag = aux;
            if(aux == true){
                nd.printTable();
                System.out.printf("\n");
            }
            else
                System.out.printf("Sem Mudanças!!!\n\n");
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
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                while(true){
                    if(inFromUser.readLine().equals("send") == true && flag == true){
                        flag = false;
                        sendVector = nd.getTable();
                        StringBuilder sendMessage = new StringBuilder();
                        sendMessage = sendMessage.append(Integer.toString(nd.getId()) + '\n');
                        for(i = 0; i < 4; i++)
                            sendMessage = sendMessage.append(Integer.toString(sendVector[i]) + '\n');
                        if(nd.getId() == 0)
                            for(i = 0; i < 3; i++){
                                Socket clientSocket = new Socket("200.9.84.95", 6521+i);
                                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                outToServer.writeBytes(sendMessage.toString());
                                clientSocket.close();
                            }
                        else if(nd.getId() == 1) {
                            Socket clientSocket = new Socket("200.9.84.95", 6520);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                            clientSocket = new Socket("200.9.84.95", 6522);
                            outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                        } else if(nd.getId() == 2) {
                            for(i = 0; i < 4; i++)
                                if(i != 2){
                                    Socket clientSocket = new Socket("200.9.84.95", 6520+i);
                                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                                    outToServer.writeBytes(sendMessage.toString());
                                    clientSocket.close();
                                }
                        } else {
                            Socket clientSocket = new Socket("200.9.84.95", 6520);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                            clientSocket = new Socket("200.9.84.95", 6522);
                            outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes(sendMessage.toString());
                            clientSocket.close();
                        }
                    } else {
                        System.out.printf("Nenhum pacote enviado(Sem Mudanças!!!)\n");
                        nd.printTable();
                        System.out.printf("\n");
                        //caso deseje parar o programa
                        String end = "y";
                        String ans;

                        System.out.println("Deseja encerrar o processo? y/n");
                        inFromUser = new BufferedReader(new InputStreamReader(System.in));
                        ans = inFromUser.readLine();
                        if(end.equals(ans))
                            System.exit(0);
                    }
                }
            }

            catch(IOException a) {
                a.printStackTrace();
            }
        }
    };

}
