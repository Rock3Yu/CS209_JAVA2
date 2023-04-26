package cn.edu.sustech.cs209.chatting.server;

import cn.edu.sustech.cs209.chatting.common.Message;
import cn.edu.sustech.cs209.chatting.server.controller.Controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ChatServer implements Runnable {

    private String username;
    private Controller controller;
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    public ChatServer(Controller controller, Socket socket) throws IOException {
        this.controller = controller;
        this.socket = socket;
        os = socket.getOutputStream();
        is = socket.getInputStream();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[1024];
                int readLen = 0;
                while ((readLen = is.read(buf)) != -1) {
                    String prompt = new String(buf, 0, readLen);
                    System.out.println("receive from client: " + prompt);
                    deal(prompt);
                }
                is.close();
            }
        } catch (IOException e) {
            try {
                socket.sendUrgentData(0);
            } catch (IOException ex) {
                controller.userController.offline(username);
                System.err.println("用户退出登录，socket已经断开连接");
            }
        }
    }

    private void deal(String prompt) throws IOException {
        String[] tmp = prompt.split(",", 2);
        String type = prompt.split(",", 2)[0];
        String body = "";
        if (tmp.length == 2) {
            body = prompt.split(",", 2)[1];
        }

        switch (type) {
            case "validName":
                boolean valid = controller.login(body, "123");
                if (valid) {
                    username = body;
                }
                os.write((String.valueOf(valid)).getBytes());
                break;
            case "onlineUsernames":
                List<String> onlineUsernames = controller.userController.getOnlineUserNames();
                os.write(String.join(",", onlineUsernames).getBytes());
                break;
            case "onlineUsernamesWithState":
                onlineUsernames = controller.userController.getOnlineUserNames();
                onlineUsernames = onlineUsernames.stream().map(e -> {
                            boolean isNull =
                                    controller.msgController.readMsgByUser.get(username + ":" + e) == null;
                            if (!isNull && !controller.msgController.readMsgByUser.get(
                                    username + ":" + e)) {
                                return e + " *";
                            }
                            return e;
                        })
                        .collect(Collectors.toList());
                os.write(String.join(",", onlineUsernames).getBytes());
                break;
            case "getEnrolledGroups":
                String[] enrolledGroupNames = controller.msgController.getEnrolledGroups(username);
                String s = String.join(",", enrolledGroupNames);
                if (enrolledGroupNames.length == 0) {
                    s = "null";
                }
                os.write(s.getBytes());
                break;
            case "sendMsg":
                Message msg = Message.parseMessage(body.split(",", 4));
                boolean ok = controller.msgController.receive(msg);
                os.write(String.valueOf(ok).getBytes());
                break;
            case "getPrivateChatHistory":
                String[] fromTo = body.split(",", 2);
                s = controller.msgController.getPrivateChatHistory(fromTo[0], fromTo[1]);
                if (s.equals("")) {
                    s = "null";
                }
                os.write(s.getBytes());
                break;
            case "createGroupChat":
                String groupMembers = body;
                String groupName = controller.msgController.createGroupChat(groupMembers);
                os.write(groupName.getBytes());
                break;
            case "getGroupChatHistory":
                groupName = body;
                String groupChatHistory = controller.msgController.getGroupChatHistory(groupName);
                os.write(groupChatHistory.getBytes());
                break;
        }
    }
}
