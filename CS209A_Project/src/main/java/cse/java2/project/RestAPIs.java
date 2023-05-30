package cse.java2.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;

public class RestAPIs {

    static String path1 = "src/main/resources/static/js/Number_3.js";
    static String path2 = "src/main/resources/static/js/Accepted_2.js";
    static String path3 = "src/main/resources/static/js/Users_2.js";

    public static String QuestionDistribution(int num) {
        String data = getJsFileContent(path1);
        if (num == -1) {
            return data;
        }
        JSONObject json = new JSONObject(data);
        if (json.has(String.valueOf(num))) {
            return String.valueOf(json.getInt(String.valueOf(num)));
        }
        return "0";
    }

    public static String AcceptedTimeDistribution(int num) {
        int[] times = new int[]{0, 10, 100, 1000, 10000, 100000, 1000000};
        String type = "1000000-";
        for (int i = 1; i < 7; i++) {
            if (num < times[i]) {
                type = times[i - 1] + "-" + times[i];
                break;
            }
        }
        String data = getJsFileContent(path2);
        if (num <= -1) {
            return data;
        }
        JSONObject json = new JSONObject(data);
        if (json.has(type)) {
            return type + " : " + json.getInt(type);
        }
        return "0";
    }

    public static String MostActiveUsersId(int min) {
        String data = getJsFileContent(path3);
        if (min <= 10) {
            return "Too much users, plz use min >= 10";
        }
        JSONObject json = new JSONObject(data);
        Map<String, Object> map = json.toMap();
        for (String key : map.keySet().toArray(new String[0])) {
            if ((Integer) map.get(key) < min) {
                map.remove(key);
            }
        }
        return map.toString();
    }

    private static String getJsFileContent(String path) {
        String line;
        StringBuilder data = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString().split("=")[1].replace(" ", "");
    }


}

