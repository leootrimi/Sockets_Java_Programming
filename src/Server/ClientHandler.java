package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server : " + clientUsername + " Connected ");

        } catch (Exception e){
            closeAll(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override
    public void run() {

        String messageClient;

        while (socket.isConnected()){
            try {
                messageClient = bufferedReader.readLine();
                broadcastMessage(messageClient);
            } catch (IOException e) {
                closeAll(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try {
                if (!clientHandler.clientUsername.equals(clientUsername));
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (Exception e){
                closeAll(socket,bufferedReader,bufferedWriter);
            }
        }
    }
    public void closeAll(Socket s,BufferedReader bf,BufferedWriter bw) {
        removeClient();
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


        public void removeClient(){
        clientHandlers.remove(this);
        System.out.println("Server " + clientUsername + "Has left");
    }
}
