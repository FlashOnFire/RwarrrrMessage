package fr.onyx;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Yoo comment t'appelles-tu RWARRR ?");
        String pseudo = sc.nextLine();

        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("On est connecté ouuuuuuuuuu RWARRRR");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

            out.println("pseudo:" + pseudo);

            Thread sendingThread = new Thread(() -> {
                while (!socket.isClosed()) {
                    out.println("message:" + sc.nextLine());
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
                } catch (Exception e) {
                    throw new RuntimeException(e);
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
