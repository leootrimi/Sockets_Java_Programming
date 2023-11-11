package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {

    private ServerSocket serverSocket;

    public ServerSide(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void start(){
        try {
        while (!serverSocket.isClosed()){
           Socket socket = serverSocket.accept();
            System.out.println("New Client connected...");
            ClientHandler clientHandler = new ClientHandler(socket);

            Thread thread = new Thread(clientHandler);
            thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeServer(){
        if (serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        int port = 4532;
        ServerSocket serverSocket1 =  new ServerSocket(port);
        ServerSide server = new ServerSide(serverSocket1);
        server.start();

    }
}