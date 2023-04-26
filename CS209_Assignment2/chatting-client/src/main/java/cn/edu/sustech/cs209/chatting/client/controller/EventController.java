package cn.edu.sustech.cs209.chatting.client.controller;

import cn.edu.sustech.cs209.chatting.common.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("checkstyle:Indentation")
public class EventController {

    public Socket socket;
    public OutputStream os;
    public InputStream is;

    public EventController() throws IOException {
        socket = new Socket("localhost", 3027);
        os = socket.getOutputStream();
        is = socket.getInputStream();
    }

    private String receive() throws IOException {
        byte[] buf = new byte[102400];
        int readLen = 0;
        String receive = "";
        if ((readLen = is.read(buf)) != -1) {
            receive = new String(buf, 0, readLen);
        }
        return receive;
    }

    public boolean validName(String name) throws IOException {
        os.write(("validName," + name).getBytes());
        return Boolean.parseBoolean(receive());
    }

    public String[] getOnlineUsernames() throws IOException {
        os.write("onlineUsernames".getBytes());
        return receive().split(",");
    }

    public String[] getOnlineUsernamesWithState() throws IOException {
        os.write("onlineUsernamesWithState".getBytes());
        return receive().split(",");
    }

    public List<String> getEnrolledGroups() throws IOException {
        os.write("getEnrolledGroups".getBytes());
        String re = receive();
        if (re.equals("null")) {
            return new ArrayList<>(0);
        }
        return Arrays.stream(re.split(",")).collect(Collectors.toList());
    }

    public boolean sendMsg(String from, String to, String data) throws IOException {
        if (to.equals("null")) {
            return false;
        }
        Message msg = new Message(new Date().getTime(), from, to, data);
        String str = Message.getStringFormat(msg);
        os.write(("sendMsg," + str).getBytes());
        return Boolean.parseBoolean(receive());
    }

    public List<Message> getPrivateChatHistory(String from, String to) throws IOException {
        if (to.equals("null")) {
            return new ArrayList<Message>(0);
        }
        os.write(("getPrivateChatHistory," + from + "," + to).getBytes());
        String re = receive();
        return strToMessageList(re);
    }

    public String createGroupChat(String groupMembers) throws IOException {
        os.write(("createGroupChat," + groupMembers).getBytes());
        return receive();
    }

    public List<Message> getGroupChatHistory(String groupName) throws IOException {
        if (groupName.equals("null")) {
            return new ArrayList<Message>(0);
        }
        os.write(("getGroupChatHistory," + groupName).getBytes());
        String re = receive();
        return strToMessageList(re);
    }

    private List<Message> strToMessageList(String re) {
        if (re.equals("null")) {
            return new ArrayList<Message>(0);
        }
        String[] histories = re.split("\n");
        return Arrays.stream(histories)
                .map(e -> {
                    if (!e.contains(",")) {
                        return null;
                    }
                    String[] tmp = e.split(",", 4);
                    String data = tmp[3].replace("@", "\n");
                    return new Message(Long.parseLong(tmp[0]), tmp[1], tmp[2], data);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
