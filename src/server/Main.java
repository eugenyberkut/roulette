package server;

import bets.Bet;
import gui.ScoreBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Поток игры - реализует механизмы регистрации, учета игроков и их баланса
 */
public class Main extends Thread {
    Random rnd = new Random();

    private List<UserAccount> userAccounts = new ArrayList<>();

    private int startMoney = 500;
    private String serverState;
    private ScoreBoard scoreBoard;
    public static final String REGISTRATION = "registration";
    public static final String RUNNING = "running";
    public static final String GAME_OVER = "game over";
    private int MAX_BET = 100;

    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

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
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    private String processCommand(String command) {
        if (GAME_OVER.equals(getServerState())) return Command.IGNORED;
        String[] split = command.split(" ");
        if (split.length<2 && !split[0].equals("state")) {
            return Command.ERROR;
        }
        switch (split[0]) {
            case "register":
                if (!serverState.equals(REGISTRATION) || exists(split[1])) {
                    return Command.REJECTED;
                } else {
                    String result = register(split[1]);
                    scoreBoard.repaint();
                    return result;
                }
            case "state":
                return getServerState();

            case "account":
                return split[1] + " " + getAccountMoney(split[1]);
            case "bet":
                String result = processBet(split);
                scoreBoard.repaint();
                return result;
            default: return Command.ERROR;
        }

    }

    private String processBet(String[] bet) {
        if (!RUNNING.equals(serverState)) {
            return Command.IGNORED;
        }
        // Ищем по ключу
        UserAccount ua = findUserAccount(bet[bet.length - 1]);
        if (ua == null) return Command.WRONG_ACCOUNT;
        Bet b = Bet.createBet(bet);
        if (b == null) return  Command.ERROR;
        if (b.getMoney() > MAX_BET) return Command.BET_TOO_BIG;
        if (b.getMoney() > ua.getMoney()) return Command.OVERDRAFT;
        ua.addBet(b);
        int money = b.getMoney();
        ua.addMoney(-money); // ставка сделана
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

//    private boolean keyExists(String s) {
//        return userAccounts.stream().anyMatch(u -> u.getKey().equals(s));
//    }

    private String register(String s) {
        int value = s.hashCode()^666;
        String key = s + value;
        UserAccount ua = new UserAccount(s,key,getStartMoney());
        userAccounts.add(ua);
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

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void doGame() {
        int value = rnd.nextInt(37); // от 0 до 36
        System.out.println("value = " + value);
        for (UserAccount userAccount : userAccounts) {
            List<Bet> bets = userAccount.getBets();
            System.out.println(userAccount.getName() + " - playing");
            int sum = 0;
            for (Bet bet : bets) {
                sum += gameBet(value, bet);
            }
            userAccount.addMoney(sum);
            userAccount.clearBets();
        }
    }

    private int gameBet(int value, Bet bet) {
        System.out.println(value + " : " + bet);
        return bet.win(value);
    }
}
