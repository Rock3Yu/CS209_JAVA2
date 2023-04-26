package cn.edu.sustech.cs209.chatting.server.controller;

import java.io.IOException;

public class Controller {

    final String path = "./chatting-server/src/main/resources";
    public UserController userController;
    public MsgController msgController;
    public NetController netController;

    public Controller() throws IOException {
        userController = new UserController(path);
        msgController = new MsgController(path, userController.getAllUserNames());
        netController = new NetController();
    }

    public boolean register(String name, String pwd) throws IOException {
        boolean re = userController.register(name, pwd);
        msgController.newRegister(userController.getAllUserNames());
        return re;
    }

    public boolean login(String name, String pwd) {
        return userController.login(name, pwd);
    }

    public boolean offline(String name) {
        return userController.offline(name);
    }

}
