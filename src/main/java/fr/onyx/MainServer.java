package fr.onyx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
    public static boolean running = true;
    public static List<ServerThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(1234)) {
            ss.setSoTimeout(1000);

            System.out.println("Server started !");

            while (running) {
                try {
                    Socket socket = ss.accept();
                    new ServerThread(socket).start();
                } catch (SocketTimeoutException e) {
                    //timeout
                }
            }
        }

    }

    public static void broadcast(String pseudo, String message) {
        clients.stream().filter((c) -> !c.pseudo.equals(pseudo)).forEach(client -> {
            client.out.println("message:" + pseudo + ":" + message);
        });

        System.out.println("[" + pseudo + "]" + " : " + message);
    }
}
