package dev.jackraidenph;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String host = "localhost";
        int port = 8080;
        String command = "GET";
        String path = "";

        switch (args.length) {
            case 1 -> host = args[0];
            case 2 -> port = Integer.parseInt(args[1]);
            case 3 -> command = args[2];
            case 4 -> path = args[3];
        }

        if ("GET".equals(command)) {
            Client.getMethod(host, port, path);
        } else if ("PUT".equals(command)) {
            Client.putMethod(host, port, path);
        } else if ("OPTIONS".equals(command)) {
            Client.optionsMethod(host, port, path);
        } else {
            System.out.println("Check the HTTP command! It should be either GET or PUT");
        }

    }
}