package cse.java2.project.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class UserAnswersService {

    static String jsPath = "./src/main/resources/static/js/";

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void distributionparticipates(String filePath) {
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
        Map<Integer, Set<Integer>> map = new HashMap<>();
        Map<Integer, Integer> comment_map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                map.putIfAbsent(item.getInt("post_id"), new HashSet<>());
                if (item.getJSONObject("owner").has("user_id")) {
                    map.get(item.getInt("post_id"))
                            .add(item.getJSONObject("owner").getInt("user_id"));
                } else {
                    map.get(item.getInt("post_id")).add(Integer.valueOf(
                            item.getJSONObject("owner").getString("display_name")
                                    .split("user")[1]));
                }
            }
            System.out.println(map);
            System.out.println("Number of questions: " + items.length());
            String fileName = jsPath + "Users_1.js";
            for (Integer key : map.keySet()) {
                comment_map.putIfAbsent(map.get(key).size(), 0);
                comment_map.putIfAbsent(0, items.length());
                comment_map.put(map.get(key).size(), comment_map.get(map.get(key).size()) + 1);
                comment_map.put(0, comment_map.get(0) - 1);
            }

            System.out.println(comment_map);

            line = "";
            data = "";
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("question_2500_upvotes.txt"))) {
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            json = new JSONObject(data);
            Map<Integer, Integer> answer_map = new HashMap<>();
            if (json.has("items")) {
                items = json.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    answer_map.put(item.getInt("question_id"), item.getInt("answer_count"));
                }
                Map<Integer, Integer> answer_map_count = new HashMap<>();
                for (Integer key : answer_map.keySet()) {
                    answer_map_count.putIfAbsent(answer_map.get(key), 0);
                    answer_map_count.put(answer_map.get(key),
                            answer_map_count.get(answer_map.get(key)) + 1);
                }
                Map<Integer, Integer> all_map = new HashMap<>();
                System.out.println("answer_map: " + answer_map);
                for (Integer key : answer_map.keySet()) {
                    if (map.get(key) != null) {
                        all_map.put(key, answer_map.get(key) + map.get(key).size());
                    } else {
                        all_map.put(key, answer_map.get(key));
                    }
                }

                Map<Integer, Integer> all_map_count = new HashMap<>();
                for (Integer key : all_map.keySet()) {
                    all_map_count.putIfAbsent(all_map.get(key), 0);
                    all_map_count.put(all_map.get(key), all_map_count.get(all_map.get(key)) + 1);
                }
                int allCount = 0;
                for (Integer key : all_map_count.keySet()) {
                    allCount += key * all_map_count.get(key);
                }
                int commentCount = 0;
                for (Integer key : comment_map.keySet()) {
                    commentCount += key * comment_map.get(key);
                }
                int answerCount = 0;
                for (Integer key : answer_map_count.keySet()) {
                    answerCount += key * answer_map_count.get(key);
                }

                double all_avg = (double) allCount / items.length();
                double comment_avg = (double) commentCount / items.length();
                double answer_avg = (double) answerCount / items.length();
                DecimalFormat df = new DecimalFormat("0.00");
                String formattedAllAvg = df.format(all_avg);
                String formattedCommentAvg = df.format(comment_avg);
                String formattedAnswerAvg = df.format(answer_avg);
                System.out.println(all_map_count);
                System.out.println(all_map);
                System.out.println();
                System.out.println(answer_map);
                System.out.println(answer_map_count);
                System.out.println("Number of questions: " + items.length());

                try {
                    FileWriter fileWriter = new FileWriter(fileName);
                    StringBuilder sb = new StringBuilder();
                    sb.append("let participate_distribution = {");
                    for (Map.Entry<Integer, Integer> entry : all_map_count.entrySet()) {
                        sb.append(entry.getKey()).append(": ").append(entry.getValue())
                                .append(", ");
                    }
                    sb.append("};\n");
                    sb.append("let comment_distribution = {");
                    for (Map.Entry<Integer, Integer> entry : comment_map.entrySet()) {
                        sb.append(entry.getKey()).append(": ").append(entry.getValue())
                                .append(", ");
                    }
                    sb.append("};\n");
                    sb.append("let answer_distribution = {");
                    for (Map.Entry<Integer, Integer> entry : answer_map_count.entrySet()) {
                        sb.append(entry.getKey()).append(": ").append(entry.getValue())
                                .append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    sb.append("};\n");
                    sb.append("let all_avg = ").append(formattedAllAvg).append(";\n");
                    sb.append("let comment_avg = ").append(formattedCommentAvg).append(";\n");
                    sb.append("let Answer_avg = ").append(formattedAnswerAvg).append(";\n");

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
    }

    public void activeUsers(String filePath) {
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
        Map<Integer, Set<Integer>> map = new HashMap<>();
        Map<Integer, Integer> comment_map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                map.putIfAbsent(item.getInt("post_id"), new HashSet<>());
                if (item.getJSONObject("owner").has("user_id")) {
                    map.get(item.getInt("post_id"))
                            .add(item.getJSONObject("owner").getInt("user_id"));
                } else {
                    map.get(item.getInt("post_id")).add(Integer.valueOf(
                            item.getJSONObject("owner").getString("display_name")
                                    .split("user")[1]));
                }
            }
            System.out.println(map);
            System.out.println("Number of questions: " + items.length());
            String fileName = jsPath + "Users_2.js";
            for (Integer key : map.keySet()) {
                comment_map.putIfAbsent(map.get(key).size(), 0);
                comment_map.putIfAbsent(0, items.length());
                comment_map.put(map.get(key).size(), comment_map.get(map.get(key).size()) + 1);
                comment_map.put(0, comment_map.get(0) - 1);
            }

            System.out.println(comment_map);

            line = "";
            data = "";
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("question_2500_upvotes.txt"))) {
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            json = new JSONObject(data);
            Map<Integer, Integer> answer_map = new HashMap<>();
            if (json.has("items")) {
                items = json.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    answer_map.put(item.getInt("question_id"), item.getInt("answer_count"));
                }
                Map<Integer, Integer> answer_map_count = new HashMap<>();
                for (Integer key : answer_map.keySet()) {
                    answer_map_count.putIfAbsent(answer_map.get(key), 0);
                    answer_map_count.put(answer_map.get(key),
                            answer_map_count.get(answer_map.get(key)) + 1);
                }
                Map<Integer, Integer> all_map = new HashMap<>();
                System.out.println("answer_map: " + answer_map);
                for (Integer key : answer_map.keySet()) {
                    if (map.get(key) != null) {
                        all_map.put(key, answer_map.get(key) + map.get(key).size());
                    } else {
                        all_map.put(key, answer_map.get(key));
                    }
                }
                all_map = sortByValue(all_map);
                System.out.println(all_map);
                System.out.println();
                System.out.println(answer_map);
                System.out.println(answer_map_count);
                System.out.println("Number of questions: " + items.length());
                Map<Integer, Integer> ans = new HashMap<>();
                for (Integer key : map.keySet()) {
                    for (Integer userid : map.get(key)) {
                        ans.putIfAbsent(userid, 0);
                        ans.put(userid, ans.get(userid) + 1);
                    }
                }
                ans.remove(-1);
                ans = sortByValue(ans);
                System.out.println("ans: " + ans);

                try {
                    FileWriter fileWriter = new FileWriter(fileName);
                    StringBuilder sb = new StringBuilder();
                    sb.append("let most_active_users = {");
                    int i = 0;
                    for (Map.Entry<Integer, Integer> entry : ans.entrySet()) {
                        sb.append("\"").append(entry.getKey()).append("\": ")
                                .append(entry.getValue())
                                .append(", ");
                        i++;
                        if (i == 20) {
                            break;
                        }
                    }
                    sb.append("};");

                    String js = sb.toString();
                    fileWriter.write(js);
                    fileWriter.close();
                    System.out.println("Successfully wrote to the file.");
                    System.out.println(map);
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        distributionparticipates("comment_2500_upvotes.txt");
//        activeUsers("comment_2500_upvotes.txt");
    }
}
