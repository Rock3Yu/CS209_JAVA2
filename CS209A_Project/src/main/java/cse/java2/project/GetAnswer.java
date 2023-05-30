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

public class GetAnswer {

    public static void getAnswer(String dataPath, boolean withBody) throws IOException {
        StringBuilder dataIn = new StringBuilder("");
        String line;
        StringBuilder dataWrite = new StringBuilder();
        StringBuilder data = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataPath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 23; i++) {
            ArrayList<Integer> answerIds = new ArrayList<>();
            for (int j = 0; j < 30 && i * 30 + j <= 669; j++) {
                answerIds.add(Integer.valueOf(
                        data.toString().split("\"accepted_answer_id\":")[i * 30 + j + 1].split(
                                ",")[0]));
            }
            String apiurl = "https://api.stackexchange.com/2.3/answers/"
                    + String.join(";", answerIds.toString()
                    .replace("[", "").replace("]", "").split(", "))
                    + "?sort=votes&site=stackoverflow";
            if (withBody) {
                apiurl += "&filter=!gNu7xhMmpbI(Pq31t_3AypWsOHv(-Thf5Ci";
            }
            URL url = new URL(apiurl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Charset", "gzip");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                dataIn.append(inputLine);
            }

            dataIn = new StringBuilder(dataIn.toString().split("\\{\"items\":\\[")[1]);
            if (i == 22) {
                dataIn = new StringBuilder(dataIn.toString().split("],\"has_more\"")[0]);
            }
            System.out.println(dataIn);
            in.close();
            dataWrite.append(dataIn);
            if (i < 22) {
                dataWrite.append(",");
            }
        }
        try {
            FileWriter writer = new FileWriter("answer_2500_upvotes.txt");
            if (withBody) {
                writer = new FileWriter("answer_2500_upvotes_withBody.txt");
            }
            writer.write(dataWrite.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getAnswer("question_2500_upvotes.txt", false);
    }

}

