package cse.java2.project;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class GetQuestion {

    public static void getData(int pageNum, String dataPath) throws IOException {
        StringBuilder data_in = new StringBuilder("{\"items\":[");
        for (int i = 0; i < pageNum; i++) {
            String u =
                    "https://api.stackexchange.com/2.3/questions?tagged=java&site=stackoverflow&filter=!BHGz6aeaHNWq0gWp8s.jeBwi6PqnsW&pagesize=100&page="
                            + (i + 1);
            URL url = new URL(u);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept-Charset", "gzip");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(connection.getInputStream()),
                            "UTF-8"));
            String inputLine;
            String page = "";
            String tail = "";
            String line = "";
            while ((inputLine = in.readLine()) != null) {
                line = inputLine;
            }
            tail = line.split("],\"has_more\"")[1];
            page = line.split("],\"has_more\"")[0].split("\\{\"items\":\\[")[1];
            data_in.append(page);
            if (i < pageNum - 1) {
                data_in.append(",");
            } else {
                data_in.append("],\"has_more\"");
                data_in.append(tail);
            }
            in.close();
        }
        try {
            FileWriter writer = new FileWriter(dataPath);
            writer.write(data_in.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getData(25, "question_2500_upvotes.js");
    }
}
