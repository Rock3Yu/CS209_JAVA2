package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Practice10 {

    public static void main(String[] args) {
        String str = "";
//        try {
//            URL url = new URL("https://pokeapi.co/api/v2/pokemon/pikachu");
//            URLConnection urlConnection = url.openConnection();
//            HttpURLConnection connection;
//            connection = (HttpURLConnection) urlConnection;
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream()));
//            StringBuilder urlString = new StringBuilder();
//            String current;
//            while ((current = in.readLine()) != null) {
//                urlString.append(current);
//            }
//            str = urlString.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String path = "Lab10/Pokemon/src/main/java/org/example/1.txt";
        try (FileReader reader = new FileReader(path);
                BufferedReader br = new BufferedReader(reader)) {
            str = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson g = new Gson();
        JsonObject obj = g.fromJson(str, JsonObject.class);
        System.out.println("Name: pikachu");
        // abilities, base_experience, forms, game_indices, height
        System.out.println(obj.get("height"));
        System.out.println(obj.get("weight"));
        System.out.println(obj.get("abilities"));


    }
}