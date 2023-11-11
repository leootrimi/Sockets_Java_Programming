package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
     private String username;
    public ClientSide(Socket socket, String username){

        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeAll(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = sc.nextLine();
                bufferedWriter.write(username + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (Exception e){
            closeAll(socket,bufferedReader,bufferedWriter);

        }
    }

    public void ListenForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroup;
                try {
                    msgFromGroup = bufferedReader.readLine();
                    System.out.println(msgFromGroup);
                }catch (Exception e){
                    closeAll(socket,bufferedReader,bufferedWriter);

                }
            }
        }).start();
    }

    public void closeAll(Socket s,BufferedReader bf,BufferedWriter bw){
        try {
            if (s != null) {
                s.close();
            }
            if (bf != null){
                bf.close();
            }
            if (bw != null){
                bw.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username : ");
        String username = sc.nextLine();
        try {
            Socket socket1 = new Socket("localhost", 4532);
            ClientSide clientSide = new ClientSide(socket1,username);
            clientSide.ListenForMessages();
            clientSide.sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
