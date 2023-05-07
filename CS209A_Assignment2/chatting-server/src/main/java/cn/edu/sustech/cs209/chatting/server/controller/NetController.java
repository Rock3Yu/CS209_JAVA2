package cn.edu.sustech.cs209.chatting.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import lombok.Getter;

@Getter
public class NetController {

    private ServerSocket serverSocket;

    public NetController() throws IOException {
        // 10.24.178.254
        serverSocket = new ServerSocket(3027);
    }
}
