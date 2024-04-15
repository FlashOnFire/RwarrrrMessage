package fr.onyx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Quel est votre pseudo ?");
        String pseudo = sc.nextLine();

        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("Connexion réussie ! (vous pouvez quittez en écrivant 'quit')");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

            out.println("pseudo:" + pseudo);

            boolean quit = false;
            Thread sendingThread = new Thread(() -> {
                while (!socket.isClosed()) {
                    String line = sc.nextLine();
                    if (line.equals("quit")) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    } else {
                        out.println("message:" + line);
                    }
                }
            });

            Thread receivingThread = new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        String line = in.readLine();

                        if (line.startsWith("message:")) {
                            String[] parts = line.split(":");
                            System.out.println("[" + parts[1] + "] : " + parts[2]);
                        }

                        Thread.sleep(50);
                    }
                } catch (Exception ignored) {
                }
            });

            sendingThread.start();
            receivingThread.start();
            receivingThread.join();
            sendingThread.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
