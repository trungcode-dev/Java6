package com.example.lab5.b1;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;

public class HttpClient {

    public static HttpURLConnection openConnection(String method, String url) throws IOException {
        var uri = URI.create(url);
        var connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestMethod(method);
        return connection;
    }

    public static byte[] readData(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();

        InputStream stream;
        if (status >= 200 && status < 300) {
            stream = connection.getInputStream();
        } else {
            stream = connection.getErrorStream();
        }

        if (stream == null) {
            throw new IOException("No response from server!");
        }

        var out = new ByteArrayOutputStream();
        var block = new byte[4096];
        while (true) {
            int n = stream.read(block);
            if (n <= 0) break;
            out.write(block, 0, n);
        }

        connection.disconnect();
        return out.toByteArray();
    }

    public static byte[] writeData(HttpURLConnection connection, byte[] data) throws IOException {
        connection.setDoOutput(true);
        try (var os = connection.getOutputStream()) {
            os.write(data);
        }
        return readData(connection);
    }
}
