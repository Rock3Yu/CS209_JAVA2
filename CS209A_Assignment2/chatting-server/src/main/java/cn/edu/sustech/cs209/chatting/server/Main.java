package cn.edu.sustech.cs209.chatting.server;


import cn.edu.sustech.cs209.chatting.server.controller.Controller;
import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting server");
        final Controller controller = new Controller();

        System.out.println("Waiting for clients to connect...");
        while (true) {
            Socket s = controller.netController.getServerSocket().accept();
            System.out.println("Client connected.");
            ChatServer newClientChatServer = new ChatServer(controller, s);
            Thread t = new Thread(newClientChatServer);
            t.start();
        }
    }
}
