package cse.java2.project.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class AcceptedAnswersService {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    static String jsPath = "./src/main/resources/static/js/";

    public void acceptedPercentage(String filePath) {
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
            int acceptedCount = 0;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("accepted_answer_id")) {
                    acceptedCount++;
                }
            }
            System.out.println("Number of questions: " + items.length());
            System.out.println("Number of accepted questions: " + acceptedCount);
            double result = (double) 100 * acceptedCount / items.length();
            double result1 = 100 - result;
            DecimalFormat df = new DecimalFormat("0.00");
            String formattedResult = df.format(result);
            System.out.println("Percentage of accepted questions: " + formattedResult);
            String fileName = jsPath + "Accepted_1.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("let Accepted_1 = {\n  \"real\": " + formattedResult + ",\n");
                fileWriter.write("  \"non-accepted\": " + result1 + "\n};");
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public void distributionResolutionTime(String filePath) {
        String line;
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder dataAnswer = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader("answer_2500.txt"))) {
            while ((line = bufferedReader.readLine()) != null) {
                dataAnswer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Integer> map = new HashMap<>();
        JSONObject json = new JSONObject(data);
        if (json.has("items")) {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("accepted_answer_id")) {
                    long questionTimestamp = item.getLong("creation_date");
                    long acceptedAnswerTimestamp = Long.parseLong(dataAnswer.toString()
                            .split(String.valueOf(item.getLong("accepted_answer_id")))[0].split(
                            "\"creation_date\":")[dataAnswer.toString()
                            .split(String.valueOf(item.getLong("accepted_answer_id")))[0].split(
                            "\"creation_date\":").length - 1].split(",\"answer_id\":")[0]);

                    //  list.add(acceptedAnswerTimestamp-questionTimestamp);
                    int length = String.valueOf(acceptedAnswerTimestamp - questionTimestamp)
                            .length();
                    if (length == 1) {
                        map.putIfAbsent("0-10", 0);
                        map.put("0-10", map.get("0-10") + 1);
                    } else if (length == 2) {
                        map.putIfAbsent("10-100", 0);
                        map.put("10-100", map.get("10-100") + 1);
                    } else if (length == 3) {
                        map.putIfAbsent("100-1000", 0);
                        map.put("100-1000", map.get("100-1000") + 1);
                    } else if (length == 4) {
                        map.putIfAbsent("1000-10000", 0);
                        map.put("1000-10000", map.get("1000-10000") + 1);
                    } else if (length == 5) {
                        map.putIfAbsent("10000-100000", 0);
                        map.put("10000-100000", map.get("10000-100000") + 1);
                    } else if (length == 6) {
                        map.putIfAbsent("100000-1000000", 0);
                        map.put("100000-1000000", map.get("100000-1000000") + 1);
                    } else if (length > 6) {
                        map.putIfAbsent("1000000-", 0);
                        map.put("1000000-", map.get("1000000-") + 1);
                    }
                }
            }
            //  map = sortByValue(map);
            //  Collections.sort(list);
            System.out.println("Number of questions: " + items.length());
            System.out.println("Distribution of question resolution time: " + map);
            System.out.println(map);
            String fileName = jsPath + "Accepted_2.js";

            try {
                StringBuilder sb = new StringBuilder();
                sb.append("let distribution = {");
                FileWriter fileWriter = new FileWriter(fileName);
                for (int i = 0; i < map.entrySet().size(); i++) {
                    if (i == 0) {
                        sb.append("\"").append("0-10").append("\": ")
                                .append(map.get("0-10")).append(", ");
                    }
                    if (i == 1) {
                        sb.append("\"").append("10-100").append("\": ")
                                .append(map.get("10-100")).append(", ");
                    }
                    if (i == 2) {
                        sb.append("\"").append("100-1000").append("\": ")
                                .append(map.get("100-1000")).append(", ");
                    }
                    if (i == 3) {
                        sb.append("\"").append("1000-10000").append("\": ")
                                .append(map.get("1000-10000")).append(", ");
                    }
                    if (i == 4) {
                        sb.append("\"").append("10000-100000").append("\": ")
                                .append(map.get("10000-100000")).append(", ");
                    }
                    if (i == 4) {
                        sb.append("\"").append("100000-1000000").append("\": ")
                                .append(map.get("100000-1000000")).append(", ");
                    }
                    if (i == 4) {
                        sb.append("\"").append("1000000-").append("\": ")
                                .append(map.get("1000000-")).append(", ");
                    }
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

    public void moreUpvotesPercentage(String filePath) {
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
            int moreCount = 0;
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.getBoolean("is_answered")) {
                    JSONArray answers = item.getJSONArray("answers");
                    int acceptedVotes = 0;
                    for (int j = 0; j < answers.length(); j++) {
                        JSONObject answer = answers.getJSONObject(j);
                        if (answer.getBoolean("is_accepted")) {
                            acceptedVotes = answer.getInt("up_vote_count");
                        }
                    }
                    for (int j = 0; j < answers.length(); j++) {
                        JSONObject answer = answers.getJSONObject(j);
                        if (!answer.getBoolean("is_accepted")) {
                            if (answer.getInt("up_vote_count") > acceptedVotes) {
                                moreCount++;
                                break;
                            }
                        }
                    }
                }
            }
            System.out.println("Number of questions: " + items.length());
            System.out.println(
                    "Number of questions have non-accepted answers that have received more upvotes "
                            +
                            "than the accepted answers: " + moreCount);
            double result = (double) 100 * moreCount / items.length();
            double result1 = 100 - result;
            DecimalFormat df = new DecimalFormat("0.00");
            String formattedResult = df.format(result);
            System.out.println(
                    "Percentage of questions have non-accepted answers that have received more upvotes "
                            + "than the accepted answers: " + formattedResult);
            String fileName = jsPath + "Accepted_3.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write("let Accepted_3 = {\n  \"real\": " + formattedResult + ",\n");
                fileWriter.write("  \"not real\": " + result1 + "\n};");
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        acceptedPercentage("question_2500_upvotes.txt");
//        distributionResolutionTime("question_2500.txt"); //用upvotes数据会有负数
//        moreUpvotesPercentage("question_2500_upvotes.txt");
    }
}
