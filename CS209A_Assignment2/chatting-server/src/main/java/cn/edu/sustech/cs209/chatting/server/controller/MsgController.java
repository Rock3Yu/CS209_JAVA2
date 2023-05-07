package cn.edu.sustech.cs209.chatting.server.controller;

import static cn.edu.sustech.cs209.chatting.server.util.Util.appendToCSV;

import cn.edu.sustech.cs209.chatting.common.Message;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

public class MsgController {

    private final String path;
    public Dictionary<String, Boolean> readMsgByUser;

    public MsgController(String path, List<String> allUserNames) throws IOException {
        this.path = path;
        mkdirs(allUserNames);
        readMsgByUser = new Hashtable<>();
        allUserNames.forEach(e1 ->
                allUserNames.forEach(e2 -> readMsgByUser.put((e1 + ":" + e2), true)));
    }

    public void newRegister(List<String> allUserNames) throws IOException {
        mkdirs(allUserNames);
    }

    public void mkdirs(@NotNull List<String> allUserNames) throws IOException {
        for (String userName : allUserNames) {
            String path1 = path + "/" + userName;
            File file1 = new File(path1);
            if (!file1.exists()) {
                boolean tmp = file1.mkdirs();
            }
            for (String userName2 : allUserNames) {
                if (userName.equals(userName2)) {
                    continue;
                }
                String path2 = path1 + "/" + userName2 + ".csv";
                File file2 = new File(path2);
                if (!file2.exists()) {
                    boolean tmp = file2.createNewFile();
                }
            }
        }
    }

    public String getPrivateChatHistory(String from, String to) {
        String path1 = path + "/" + from + "/" + to + ".csv";
        readMsgByUser.put(from + ":" + to, true);
        return pathToGetChatHistory(path1);
    }

    public String getGroupChatHistory(String groupNames) {
        String path1 = path + "/group_chats/" + groupNames + ".csv";
        return pathToGetChatHistory(path1);
    }

    private String pathToGetChatHistory(String path1) {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(path1);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                stringBuilder.append(readLine).append("\n");
            }
        } catch (IOException e) {
            System.err.println("getPrivateChatHistory出现错误，读文件错误");
        }
        return stringBuilder.toString();
    }

    public String createGroupChat(String groupMembers) throws IOException {
        String path1 = path + "/group_chats/group";
        int idx = 1;
        while (new File(path1 + idx + ".csv").exists()) {
            idx++;
        }
        path1 += idx + ".csv";
        if (new File(path1).createNewFile()) {
            String firstLine = new Message("notify", "all",
                    "The group members:[" + groupMembers + "]").toString();
            appendToCSV(path1, firstLine + "\n");
            return "group" + idx;
        }
        return "false";
    }

    public List<List<Message>> reloadMessageList(String name, @NotNull List<String> allUserNames) {
        String path1 = path + "/" + name;
        List<List<Message>> re = new ArrayList<>();
        for (String u : allUserNames) {
            if (u.equals(name)) {
                continue;
            }
            List<Message> re1 = new ArrayList<>();
            String path2 = path1 + "/" + u + ".txt";
            try (Reader reader = Files.newBufferedReader(Paths.get(path2))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
                records.forEach(record -> {
                    Message m = new Message(Long.parseLong(record.get(0)),
                            record.get(1), record.get(2), record.get(3));
                    re1.add(m);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (re1.size() != 0) {
                re.add(re1);
            }
        }
        return re;
    }

    public boolean receive(Message msg) {
        if (!msg.getSendTo().toLowerCase().contains("group")) {
            return receiveFromPrivateChat(msg);
        } else {
            return receiveFromGroupChat(msg);
        }
    }

    private boolean receiveFromPrivateChat(Message msg) {
        String from = msg.getSentBy();
        String to = msg.getSendTo();
        String path1 = path + "/" + from + "/" + to + ".csv";
        String path2 = path + "/" + to + "/" + from + ".csv";
        msg.setData(msg.getData().replace("\n", "@"));
        appendToCSV(path1, msg.toString() + "\n");
        appendToCSV(path2, msg.toString() + "\n");
        readMsgByUser.put((to + ":" + from), false);
        return true;
    }

    private boolean receiveFromGroupChat(Message msg) {
        String path1 = path + "/group_chats/" + msg.getSendTo() + ".csv";
        Message msg1 = new Message(msg.getTimestamp(), msg.getSentBy(), "all", msg.getData());
        appendToCSV(path1, msg1.toString() + "\n");
        return true;
    }

    public boolean receive(Long timestamp, String from, String to, String data) {
        return receive(new Message(timestamp, from, to, data));
    }

    public String[] getEnrolledGroups(String username) {
        String path1 = path + "/group_chats/group";
        int idx = 1;
        List<String> re = new ArrayList<>();
        while (true) {
            File f = new File(path1 + idx + ".csv");
            if (!f.exists()) {
                break;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String readLine = reader.readLine();
                if (readLine.contains(username)) {
                    re.add("group" + idx);
                }
            } catch (IOException e) {
                System.err.println("getEnrolledGroups()出现错误，读文件错误");
            }
            idx++;
        }
        return re.toArray(new String[0]);
    }
}
