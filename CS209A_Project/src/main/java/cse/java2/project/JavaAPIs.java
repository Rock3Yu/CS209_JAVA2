package cse.java2.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JavaAPIs {

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

    public static void getAPIFrequency() {
        String dataFilePath = "answer_2500_upvotes_withBody.txt";
        String keysFilePath = "./src/main/resources/static/files/AllClasses.txt";

        try {
            String data = readDataFromFile(dataFilePath);
            Map<String, Integer> keyFrequencyMap = countKeyFrequency(data, keysFilePath);

            // 输出每个键的出现次数
            for (Map.Entry<String, Integer> entry : keyFrequencyMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Frequency: " + entry.getValue());
            }
            keyFrequencyMap = sortByValue(keyFrequencyMap);
            String fileName = jsPath + "API_frequency.js";

            try {
                FileWriter fileWriter = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append("let distribution = {\n");
                for (Map.Entry<String, Integer> entry : keyFrequencyMap.entrySet()) {
                    sb.append("  \"").append(entry.getKey()).append("\": ").append(entry.getValue())
                            .append(", \n");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readDataFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());
        }

        reader.close();
        return sb.toString();
    }

    private static Map<String, Integer> countKeyFrequency(String data, String keysFilePath)
            throws IOException {
        Map<String, Integer> keyFrequencyMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(keysFilePath));
        String key;

        while ((key = reader.readLine()) != null) {
            int frequency = countOccurrences(data, key);
            keyFrequencyMap.put(key, frequency);
        }

        reader.close();
        return keyFrequencyMap;
    }

    private static int countOccurrences(String data, String key) {
        int count = 0;
        int index = data.indexOf(key);

        while (index != -1) {
            count++;
            index = data.indexOf(key, index + key.length());
        }
        return count;
    }

    public static void main(String[] args) {
        getAPIFrequency();

    }


}

