package cn.edu.sustech.cs209.chatting.common;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    final private Long timestamp;

    final private String sentBy;

    final private String sendTo;

    private String data;

    public Message(Long timestamp, String sentBy, String sendTo, String data) {
        this.timestamp = timestamp;
        this.sentBy = sentBy;
        this.sendTo = sendTo;
        this.data = data;
    }

    public Message(String sentBy, String sendTo, String data) {
        this.timestamp = new Date().getTime();
        this.sentBy = sentBy;
        this.sendTo = sendTo;
        this.data = data;
    }

    @Override
    public String toString() {
        return timestamp + "," +
                sentBy + "," +
                sendTo + "," +
                data;
    }

    public static String getStringFormat(Message msg){
        return msg.getTimestamp() + "," +
                msg.getSentBy()+ "," +
                msg.getSendTo() + "," +
                msg.getData();
    }

    public static Message parseMessage(String[] strs) {
        return new Message(Long.parseLong(strs[0]), strs[1], strs[2], strs[3]);
    }
}
