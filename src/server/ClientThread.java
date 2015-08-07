package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Поток общения с клиентом
 */
public class ClientThread extends Thread {
    private Socket socket;
    private Main roulette;

    public ClientThread(Socket socket, Main roulette) {
        this.socket = socket;
        this.roulette = roulette;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = in.readLine();
            out.println(roulette.processCommand(command));
        } catch (Exception ex) {
            System.err.println("error in thread");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
