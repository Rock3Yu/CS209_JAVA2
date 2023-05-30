package cse.java2.project.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TagsAnswersService {

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

    public void distributionTags(String filePath) {
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
        Map<String, Integer> map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Object object = item.get("tags");
//                System.out.println(object.toString());
                String[] strings = object.toString().split(",");
                for (int j = 1; j < strings.length - 1; j++) {
                    map.putIfAbsent(strings[j], 0);
                    map.put(strings[j], map.get(strings[j]) + 1);
                }
                if (strings.length > 1) {
                    map.putIfAbsent(strings[strings.length - 1].split("]")[0], 0);
                    map.put(strings[strings.length - 1].split("]")[0],
                            map.get(strings[strings.length - 1].split("]")[0]) + 1);
                }
            }
            map = sortByValue(map);
            System.out.println(map);
            System.out.println("Number of questions: " + items.length());
            String fileName = jsPath + "Tags_1.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("let tag_1_raw = {");
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    sb.append("" + entry.getKey() + ": " + entry.getValue() + ", ");
                }
                sb.setLength(sb.length() - 2);
                sb.append("};");

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

    public void distributionUpvotes(String filePath) {
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
        Map<String, Integer> map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Object object = item.get("tags");
                String tags = object.toString();
                String tagString = tags.replace("[", "").replace("]", "").replace("\"", "");
                String[] tagArray = tagString.split(",");
                String result = Arrays.stream(tagArray).sorted()
                        .limit(15).collect(Collectors.joining("&"));
                map.putIfAbsent(result, 0);
                if (item.getBoolean("is_answered")) {
                    JSONArray answers = item.getJSONArray("answers");
                    for (int j = 0; j < answers.length(); j++) {
                        JSONObject answer = answers.getJSONObject(j);
                        map.put(result, map.get(result) + answer.getInt("up_vote_count"));
                    }
                }

            }
            map = sortByValue(map);
            System.out.println(map);
            System.out.println("Number of questions: " + items.length());
            String fileName = jsPath + "Tags_2.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("let tag_2_raw = {");
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    sb.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue())
                            .append(", ");
                }
                sb.setLength(sb.length() - 2);
                sb.append("};");

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

    public void distributionViews(String filePath) {
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
        Map<String, Integer> map = new HashMap<>();
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Object object = item.get("tags");
                String tags = object.toString();
                String tagString = tags.replace("[", "").replace("]", "").replace("\"", "");
                String[] tagArray = tagString.split(",");
                String result = Arrays.stream(tagArray).sorted()
                        .limit(15).collect(Collectors.joining("&"));
                System.out.println(result);
                map.putIfAbsent(result, 0);
                map.put(result, map.get(result) + item.getInt("view_count"));
            }
            map = sortByValue(map);
            System.out.println("Number of questions: " + items.length());
            String fileName = jsPath + "Tags_3.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("let tag_3_raw = {");
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    sb.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue())
                            .append(", ");
                }
                sb.setLength(sb.length() - 2);
                sb.append("};");

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


    public static void main(String[] args) throws IOException {
//        distributionTags("question_2500_upvotes.txt");
//        distributionUpvotes("question_2500_upvotes.txt");
//        distributionViews("question_2500_upvotes.txt");
    }
}
