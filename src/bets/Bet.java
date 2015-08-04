package bets;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eugen on 31.07.2015.
 */
public abstract class Bet {
    public static final int RED = 1;
    public static final int BLACK = 0;
    private char type;
    private Integer[] values;
    private int money;
    public static final List<Integer> REDS = Arrays.asList(1,3,5,7,9,12,14,16,18,21,23,25,27,28,30,32,34,36);

    public char getType() {
        return type;
    }

    public Integer[] getValues() {
        return values;
    }

    public int getMoney() {
        return money;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setValues(Integer[] values) {
        this.values = values;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public static Bet createBet(String[] bet) {
        switch (bet[0].charAt(0)) {
            case 'A': return createBetA(bet);
            case 'B': return createBetB(bet);
            case 'C': return createBetC(bet);
            case 'D': return createBetD(bet);
            case 'F': return createBetF(bet);
            case 'G': return createBetG(bet);
            case 'H': return createBetH(bet);
            case 'I': return createBetI(bet);
            case 'J': return createBetJ(bet);
            case 'K': return createBetK(bet);
            default: return null;
        }
    }

    private static Bet createBetA(String[] bet) {
        // A <number> <amount> <key>
        int number = Integer.parseInt(bet[1]);
        int amount = Integer.parseInt(bet[2]);
        return new BetA(number, amount);
    }

    private static Bet createBetB(String[] bet) {
        // B <number> <number> <amount> <key>
        int first = Integer.parseInt(bet[1]);
        int second = Integer.parseInt(bet[2]);
        if (first == 0 && second > 0 && second < 4 ||
                second - first == 1 && first % 3 != 0 ||
                second - first == 3) {
            int amount = Integer.parseInt(bet[3]);
            return new BetB(first, second, amount);
        } else return null; // Недопустимая ставка

    }

    private static Bet createBetC(String[] bet) {
        // C <number> <number> <number> <amount> <key>
        int first = Integer.parseInt(bet[1]);
        int second = Integer.parseInt(bet[2]);
        int third = Integer.parseInt(bet[3]);
        if (first % 3 == 1 && second - first == 1 && third - second == 1) {
            int amount = Integer.parseInt(bet[4]);
            return new BetC(first, second, third, amount);
        } else return null;
    }

    private static Bet createBetD(String[] bet) {
        // D <number> <number> <number> <number> <amount> <key>
        int first = Integer.parseInt(bet[1]);
        int second = Integer.parseInt(bet[2]);
        int third = Integer.parseInt(bet[3]);
        int fourth = Integer.parseInt(bet[4]);
        if (first % 3 != 0 && second-first==1 && third-first==3 && fourth-second==3) {
            int amount = Integer.parseInt(bet[5]);
            return new BetD(first, second, third, fourth, amount);
        } else return null;
    }

    private static Bet createBetF(String[] bet) {
        // F <first number> <last number> <amount> <key>
        int first = Integer.parseInt(bet[1]);
        int second = Integer.parseInt(bet[2]);
        if (first % 3 == 1 && second == first + 5) {
            int amount = Integer.parseInt(bet[3]);
            return new BetF(first, second, amount);
        } else return null; // Недопустимая ставка
    }

    private static Bet createBetG(String[] bet) {
        // G <column number from 1 to 3> <amount> <key>
        int column = Integer.parseInt(bet[1]);
        if (column > 0 && column < 4) {
            int amount = Integer.parseInt(bet[2]);
            return new BetG(column, amount);
        } else return null;
    }

    private static Bet createBetH(String[] bet) {
        // H <number of dozen from 1 to 3> <amount> <key>
        int dozen = Integer.parseInt(bet[1]);
        if (dozen > 0 && dozen < 4) {
            int amount = Integer.parseInt(bet[2]);
            return new BetH(dozen, amount);
        } else return null;
    }

    private static Bet createBetI(String[] bet) {
        // I <RED|BLACK> <amount> <key>
        int value = Bet.BLACK;
        if (bet[1].equalsIgnoreCase("RED")) {
            value = Bet.RED;
        }
        int amount = Integer.parseInt(bet[2]);
        return new BetI(value, amount);

    }
    private static Bet createBetJ(String[] bet) {
        // J <EVEN|ODD> <amount> <key>)
        int value = (bet[1].equalsIgnoreCase("EVEN")) ? 0 : 1;
        int amount = Integer.parseInt(bet[2]);
        return new BetJ(value, amount);
    }
    private static Bet createBetK(String[] bet) {
        // K <LOW|HIGH> <amount> <key>
        int value = (bet[1].equalsIgnoreCase("LOW ")) ? 0 : 1;
        int amount = Integer.parseInt(bet[2]);
        return new BetK(value, amount);
    }


    public abstract int win(int number);
}
