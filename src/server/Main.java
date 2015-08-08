package server;

import bets.Bet;
import gui.ScoreBoard;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Поток игры - реализует механизмы регистрации, учета игроков и их баланса
 */
public class Main extends Thread {
    Random rnd = new Random();
    Integer value = -1;

    private List<UserAccount> userAccounts = new ArrayList<>();

    private int startMoney = 500;
    private int port = 666;
    private String serverState;
    private ScoreBoard scoreBoard;
    public static final String REGISTRATION = "registration";
    public static final String RUNNING = "running";
    public static final String GAME_OVER = "game over";
    private int maxBet = 100;

    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public static void main(String[] args) {
        new Main().run();

    }

    @Override
    public void run() {
        try {
//            Properties properties = new Properties();
//            properties.load(new FileReader("roulette.ini"));
//            port = Integer.parseInt(properties.getProperty("port", "666"));
//            startMoney = Integer.parseInt(properties.getProperty("startmoney", "500"));
//            maxBet = Integer.parseInt(properties.getProperty("maxbet", "100"));
            ServerSocket serverSocket = new ServerSocket(port);
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
            try {
                Socket socket = serverSocket.accept();
                new ClientThread(socket, this).start();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

//    private void processMessages(ServerSocket serverSocket) throws IOException {
//        while (true) {
//            try (Socket socket = serverSocket.accept()) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                String command = in.readLine();
//                out.println(processCommand(command));
//                //socket.close();
//            } catch (Exception ex) {
//                System.err.println(ex.getMessage());
//            }
//        }
//    }

    public synchronized String processCommand(String command) {
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
                    return "ok " + result;
                }
            case "unregister": {
                String result = unregister(split[1], split[2]);
                if (result.endsWith("ok")) scoreBoard.repaint();
                return result;
            }
            case "state":
                return getServerState();

            case "account":
                return split[1] + " " + getAccountMoney(split[1]) + " " + getAccountBets(split[1]);
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
        if (b == null) return  Command.DESCRIPTION_ERROR;
        if (b.getMoney() > maxBet) return Command.BET_TOO_BIG;
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

    private int getAccountBets(String s) {
        Optional<UserAccount> account = userAccounts.stream().filter(u -> s.equals(u.getKey())).findFirst();
        if (account.isPresent()) {
            return account.get().getBets().size();
        } else {
            return -1;
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

    private String unregister(String userName, String userKey) {
        int value = userName.hashCode()^666;
        String key = userName + value;
        if (userKey.equals(key)) {
            Optional<UserAccount> first = userAccounts.stream().filter(u -> u.getName().equals(userName)).findFirst();
            if (first.isPresent()) {
                userAccounts.remove(first.get());
                return "unregister ok";
            } else return "unregister failed - user not found";
        } else return "unregister failed - incorrect name or key";
    }

    private boolean exists(String s) {
        return userAccounts.stream().anyMatch(u -> u.getName().equals(s));
    }

    public int getStartMoney() {
        return startMoney;
    }

    public String getServerState() {
        return serverState + (value == -1 ? "" : (" "+value));
    }

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void doGame() {
        value = rnd.nextInt(37); // от 0 до 36
        log("value = " + value);
        for (UserAccount userAccount : userAccounts) {
            List<Bet> bets = userAccount.getBets();
            int sum = 0;
            if (userAccount.getBets().isEmpty()) {
                log(userAccount.getName() + " - not playing - penalty 5 chips (if positive)");
                sum = -5;
                //if (sum < 0) sum = 0;
            } else {
                for (Bet bet : bets) {
                    sum += gameBet(value, bet);
                }
                if (sum > 0) {
                    log(userAccount.getName() + " - playing: win " + sum);
                } else {
                    log(userAccount.getName() + " - playing: lose");
                }
            }
            userAccount.addMoney(sum);
            int money = userAccount.getMoney();
            if (money<0) userAccount.addMoney(Math.abs(money));
            userAccount.clearBets();
        }
    }

    public static void log(String s) {
        System.out.println(s);
    }

    private int gameBet(int value, Bet bet) {
        int win = bet.win(value);
        log(value + " : " + bet + (win > 0 ? " win " + win : " lose"));
        return win;
    }

    public void setProps(int port, int startMoney, int maxBet) {
        this.port = port;
        this.startMoney = startMoney;
        this.maxBet = maxBet;
    }
}
