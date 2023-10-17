package dev.jackraidenph;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Client {

    public static void getMethod(String host, int port, String path) throws IOException {

        Socket clientSocket = new Socket(host, port);

        PrintWriter request = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader response = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        request.print("GET /" + path + "/ HTTP/1.1\r\n");
        request.print("Host: " + host + "\r\n");
        //request.print("Connection: close\r\n");
        request.print("Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n");
        request.print("\r\n");
        request.flush();

        String responseLine;
        boolean content = false;
        while ((responseLine = response.readLine()) != null) {
            if (content) {
                byte[] decoded = Base64.getDecoder().decode(responseLine);
                try (FileOutputStream fis = new FileOutputStream(path)) {
                    fis.write(decoded);
                }
            }
            if (responseLine.isBlank()) {
                content = true;
            }
        }

        response.close();
        clientSocket.close();
    }

    public static void putMethod(String host, int port, String file) throws IOException {

        Socket clientSocket = new Socket(host, port);

        PrintWriter request = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader response = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String extension = file.substring(file.lastIndexOf('.') + 1, file.length() - 1);

        request.print("PUT /" + file + "/ HTTP/1.1\r\n");
        request.print("Host: " + host + "\r\n");
        request.print("Accept-Language: en-us\r\n");
        request.print("Connection: Keep-Alive\r\n");
        request.print("Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n");
        request.print("Content-type: %s\r\n".formatted(ContentTypes.getType(extension)));
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            request.print("Content-Length: %d\r\n".formatted(bytes.length));
            request.print("\r\n");
            String content = Base64.getEncoder().encodeToString(bytes);
            request.println(content);
        }

        String responseLine;
        while ((responseLine = response.readLine()) != null) {
            System.out.println(responseLine);
        }

        request.close();
        response.close();
        clientSocket.close();
    }

    public static void optionsMethod(String host, int port, String file) throws IOException {

        Socket clientSocket = new Socket(host, port);

        PrintWriter request = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader response = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        request.print("OPTIONS /" + file + "/ HTTP/1.1\r\n");
        request.print("Host: " + host + "\r\n");
        request.print("Accept-Language: en-us\r\n");
        request.print("Connection: Keep-Alive\r\n");
        request.println("Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n");
        request.flush();

        String responseLine;
        while ((responseLine = response.readLine()) != null) {
            System.out.println(responseLine);
        }

        request.close();
        response.close();
        clientSocket.close();
    }

    private enum ContentTypes {

        NONE(""),

        HTML("text/html"),
        CSS("text/css"),
        JS("applications/javascript"),
        PNG("image/png"),
        SVG("image/svg+xml");

        public static ContentTypes getType(String extension) {
            return switch (extension) {
                case "css" -> CSS;
                case "js" -> JS;
                case "png" -> PNG;
                case "svg" -> SVG;
                default -> HTML;
            };
        }

        private final String text;

        ContentTypes(String text) {
            this.text = text;
        }
    }
}
