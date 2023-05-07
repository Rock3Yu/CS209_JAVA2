package cn.edu.sustech.cs209.chatting.server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Util {

    public static void appendToCSV(String path, String data) {
        try {
            File f = new File(path);
            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
