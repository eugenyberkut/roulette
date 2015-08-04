package server;

import bets.Bet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by eugen on 31.07.2015.
 */
public class Main extends Thread {

    List<UserAccount> userAccounts = new ArrayList<>();
    private int startMoney = 500;
    private String serverState;

    public static void main(String[] args) {
        new Main().run();

    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(666);
            processMessages(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setServerState(String serverState) {
        this.serverState = serverState;
    }

    private void processMessages(ServerSocket serverSocket) throws IOException {
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String command = in.readLine();
                out.println(processCommand(command));
                //socket.close();
            }
        }
    }

    private String processCommand(String command) {
        String[] split = command.split(" ");
        if (split.length<2 && !split[0].equals("state")) {
            return Command.ERROR;
        }
        switch (split[0]) {
            case "register":
                if (!serverState.equals("registration") || exists(split[1])) {
                    return Command.REJECTED;
                } else {
                    return register(split[1]);
                }
            case "state":
                return getServerState();

            case "account":
                return split[1] + " " + getAccountMoney(split[1]);
            case "bet":
                return processBet(split);
            default: return Command.ERROR;
        }
    }

    private String processBet(String[] bet) {
        if (!serverState.equals("running")) {
            return Command.IGNORED;
        }
        UserAccount ua = findUserAccount(bet[bet.length - 1]);
        if (ua == null) return Command.ERROR;
        Bet b = Bet.createBet(bet);
        if (b == null) return  Command.ERROR;
        ua.addBet(b);
        return Command.ACCEPTED;
    }

    private UserAccount findUserAccount(String s) {
        Optional<UserAccount> account = userAccounts.stream().filter(u -> s.equals(u.getKey())).findFirst();
        return account.isPresent() ? account.get() : null;
    }

    private int getAccountMoney(String s) {
        Optional<UserAccount> account = userAccounts.stream().filter(u -> s.equals(u.getKey())).findFirst();
        if (account.isPresent()) {
            return account.get().getMoney();
        } else {
            return 0;
        }
    }

    private boolean keyExists(String s) {
        return userAccounts.stream().anyMatch(u -> u.getKey().equals(s));
    }

    private String register(String s) {
        int value = s.hashCode()^666;
        String key = s + value;
        UserAccount ua = new UserAccount(s,key,getStartMoney());
        return key;
    }

    private boolean exists(String s) {
        return userAccounts.stream().anyMatch(u -> u.getName().equals(s));
    }

    public int getStartMoney() {
        return startMoney;
    }

    public String getServerState() {
        return serverState;
    }
}
