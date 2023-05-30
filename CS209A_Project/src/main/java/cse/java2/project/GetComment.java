package cse.java2.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetComment {

    public static void getComment(String dataPath) throws IOException {
        StringBuilder dataIn = new StringBuilder("{\"items\":[");
        String line;
        StringBuilder dataWrite = new StringBuilder();
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataPath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(data);
        JSONArray items = json.getJSONArray("items");
        for (int i = 0; i < 84; i++) {
            ArrayList<Integer> questionIds = new ArrayList<>();
            for (int j = 0; j < 30 && i * 30 + j <= 2499; j++) {
                questionIds.add(items.getJSONObject(i * 30 + j).getInt("question_id"));
            }
            String apiUrl = "https://api.stackexchange.com/2.3/questions/" + questionIds.toString()
                    .replace("[", "").replace("]", "").replace(", ", ";")
                    + "/comments?order=desc&sort=votes&site=stackoverflow&filter=withbody";
            URL url = new URL(apiUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Charset", "gzip");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                dataIn.append(inputLine);
            }

            dataIn = new StringBuilder(dataIn.toString().split("\\{\"items\":\\[")[1]);
            if (i < 83) {
                dataIn = new StringBuilder(dataIn.toString().split("],\"has_more\"")[0]);
            }
            System.out.println(dataIn);
            in.close();
            dataWrite.append(dataIn);
            if (i < 83) {
                dataWrite.append(",");
            }
            //  System.out.println(dataIn);
        }
        try {
            FileWriter writer = new FileWriter("comment_2500_upvotes.txt");
            writer.write(dataWrite.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getComment("question_2500_upvotes.txt");

    }

}

