package com.example.lab5.b1;

import java.nio.charset.StandardCharsets;

public class RestClient {

    static String host = "https://java6-c91d4-default-rtdb.firebaseio.com";
    static String lastKey;

    private static void getAll() {
        var url = host + "/students.json";
        try {
            var con = HttpClient.openConnection("GET", url);
            var resp = HttpClient.readData(con);
            System.out.println("GET ALL:");
            System.out.println(new String(resp, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void getByKey(String key) {
        var url = host + "/students/" + key + ".json";
        try {
            var con = HttpClient.openConnection("GET", url);
            var resp = HttpClient.readData(con);
            System.out.println("GET BY KEY:");
            System.out.println(new String(resp, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void post() {
        var url = host + "/students.json";
        var json = """
                {
                    "id": "SV09",
                    "name": "Sinh viên 09",
                    "mark": 5.5,
                    "gender": true
                }
                """;
        try {
            var con = HttpClient.openConnection("POST", url);
            var resp = HttpClient.writeData(con, json.getBytes(StandardCharsets.UTF_8));
            System.out.println("POST:");
            String responseStr = new String(resp, StandardCharsets.UTF_8);
            System.out.println(responseStr);

            lastKey = responseStr.replaceAll(".*\"name\"\\s*:\\s*\"([^\"]+)\".*", "$1");
            System.out.println("Generated Key: " + lastKey);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void put(String key) {
        var url = host + "/students/" + key + ".json";
        var json = """
                {
                    "id": "SV09",
                    "name": "Sinh viên UPDATED",
                    "mark": 7.2,
                    "gender": false
                }
                """;
        try {
            var con = HttpClient.openConnection("PUT", url);
            var resp = HttpClient.writeData(con, json.getBytes(StandardCharsets.UTF_8));
            System.out.println("PUT:");
            System.out.println(new String(resp, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void delete(String key) {
        var url = host + "/students/" + key + ".json";
        try {
            var con = HttpClient.openConnection("DELETE", url);
            HttpClient.readData(con);
            System.out.println("DELETE: Đã xóa key " + key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getAll();
        post();
        getByKey(lastKey);
        put(lastKey);
        delete(lastKey);
        getAll();
    }
}
