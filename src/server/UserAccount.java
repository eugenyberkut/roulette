package server;

import bets.Bet;

import java.util.ArrayList;
import java.util.List;

/**
 * Аккаунт игрока
 */
public class UserAccount {
    private String name;
    private String key;
    private int money;

    private List<Bet> bets = new ArrayList<>();

    public UserAccount(String name, String key, int money) {
        this.name = name;
        this.key = key;
        this.money = money;
    }

    public void addBet(Bet b) {
        bets.add(b);
    }

    public void clearBets() {
        bets.clear();
    }

    public int addMoney(int amount) {
        money += amount;
        return money;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public int getMoney() {
        return money;
    }

    public List<Bet> getBets() {
        return bets;
    }
}
