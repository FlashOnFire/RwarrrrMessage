package fr.onyx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    public Socket socket;
    public String pseudo = null;

    BufferedReader in;
    PrintWriter out;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (!socket.isClosed()) {
                String line = in.readLine();
                if (line.startsWith("pseudo:")) {
                    if (pseudo == null) {
                        pseudo = line.substring(7);
                        MainServer.broadcast("Server", pseudo + " s'est connecté !");
                        MainServer.clients.add(this);
                    }
                } else if (line.startsWith("message:")) {
                    MainServer.broadcast(pseudo, line.substring(8));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
