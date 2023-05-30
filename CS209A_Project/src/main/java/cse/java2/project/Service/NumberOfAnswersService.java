package cse.java2.project.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class NumberOfAnswersService {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    static String jsPath = "./src/main/resources/static/js/";

    public void unansweredPercentage(String filePath) {
        String line;
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(data);
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            int unansweredCount = 0;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                boolean isAnswered = item.getBoolean("is_answered");
                if (!isAnswered) {
                    unansweredCount++;
                }
            }
            System.out.println("Number of questions: " + items.length());
            System.out.println("Number of unanswered questions: " + unansweredCount);
            double result = (double) 100 * unansweredCount / items.length();
            double answered_result = 100 - result;
            DecimalFormat df = new DecimalFormat("0.00");
            String formattedResult = df.format(result);
            String formattedAnswer = df.format(answered_result);
            System.out.println("Percentage of unanswered questions: " + formattedResult);
            String fileName = jsPath + "Number_1.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("Number_1 = {\n  \"non-answered\": " + formattedResult
                        + ",\n  \"answered\": " + formattedAnswer + "\n};");
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public void average_maximumNum(String filePath) {
        String line;
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(data);
        int max_answer_count = 0;
        int all_answer_count = 0;
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                int answer_count = item.getInt("answer_count");
                all_answer_count += answer_count;
                max_answer_count = Math.max(max_answer_count, answer_count);
            }
            double average_num = (double) all_answer_count / items.length();
            DecimalFormat df = new DecimalFormat("0.00");
            System.out.println("Number of questions: " + items.length());
            System.out.println("Average number of answers: " + df.format(average_num));
            System.out.println("Maximum number of answers: " + max_answer_count);
            String fileName = jsPath + "Number_2.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("Number_2_raw = {\n  \"avg\": " + df.format(average_num) + ",\n");
                fileWriter.write("  \"max\": " + max_answer_count + "\n};");
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public void distributionNum(String filePath) {
        String line;
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(data);
//        List<Integer> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                int answer_count = item.getInt("answer_count");
                map.putIfAbsent(answer_count, 0);
                map.put(answer_count, map.get(answer_count) + 1);
//                list.add(answer_count);
            }
//            map = sortByValue(map);
            System.out.println(map);
//            Collections.sort(list);
            System.out.println("Number of questions: " + items.length());
            System.out.println("Distribution of the number of answers: " + map);
            String fileName = jsPath + "Number_3.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("let distribution = {\n");
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    sb.append("  " + entry.getKey() + ": " + entry.getValue() + ", \n");
                }
                sb.setLength(sb.length() - 3);
                sb.append("\n};");

                String js = sb.toString();
                fileWriter.write(js);
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        unansweredPercentage("question_2500_upvotes.txt");
//        average_maximumNum("question_2500_upvotes.txt");
//        distributionNum("question_2500_upvotes.txt");
    }
}
