package cn.edu.sustech.cs209.chatting.server.controller;

import cn.edu.sustech.cs209.chatting.server.entity.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class UserController {

    private String path;
    private List<User> AllUsers = new ArrayList<>();
    private List<User> OnlineUsers = new ArrayList<>();
    private List<User> OfflineUsers = new ArrayList<>();

    public UserController(String path) {
        this.path = path;
        try (Reader reader = Files.newBufferedReader(Paths.get(path + "/users.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            records.forEach(record -> {
                User u = new User(record.get(0), record.get(1));
                AllUsers.add(u);
                OfflineUsers.add(u);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean register(String name, String pwd) {
        boolean repeat = AllUsers.stream().map(User::getName).anyMatch(name::equals);
        if (!repeat) {
            try {
                File f = new File(path + "/users.csv");
                BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
                out.write(name + "," + pwd + "\n");
                out.flush();
                out.close();
                System.out.println("注册成功");
                User u = new User(name, pwd);
                AllUsers.add(u);
                OnlineUsers.add(u);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("注册失败，发现未知错误");
                return false;
            }
        } else {
            System.err.println("该用户名已存在，请更换用户名");
            return false;
        }
    }

    public boolean login(String name, String pwd) {
        User u = new User(name, pwd);
        if (OfflineUsers.contains(u)) {
            System.out.println("登陆成功");
            OfflineUsers.remove(u);
            OnlineUsers.add(u);
            return true;
        } else if (OnlineUsers.contains(u)) {
            System.err.println("用户已登录，请勿重复登陆");
            return false;
        }
        System.err.println("用户名密码错误，或未注册");
        return false;
    }

    public boolean offline(String name) {
        User null1 = new User("null", "null");
        User u = OnlineUsers.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst().orElse(null1);
        if (!u.equals(null1)) {
            OnlineUsers.remove(u);
            OfflineUsers.add(u);
            System.out.println(u.getName() + " 已下线");
            return true;
        } else {
            System.err.println("下线失败，发现未知错误");
            return false;
        }
    }

    public List<String> getAllUserNames() {
        return AllUsers.stream().map(User::getName).collect(Collectors.toList());
    }

    public List<String> getOnlineUserNames() {
        return OnlineUsers.stream().map(User::getName).collect(Collectors.toList());
    }

}
