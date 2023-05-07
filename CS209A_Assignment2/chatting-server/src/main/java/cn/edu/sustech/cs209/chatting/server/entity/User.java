package cn.edu.sustech.cs209.chatting.server.entity;

import java.util.Objects;
import lombok.Getter;

@Getter
public class User {

    private final String name;
    private final String pwd;

    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(pwd, user.pwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pwd);
    }
}
