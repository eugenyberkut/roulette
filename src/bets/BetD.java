package bets;

import java.util.Arrays;

/**
 * Created by eugen on 04.08.2015.
 */
public class BetD extends Bet {
    public BetD(int first, int second, int third, int fourth, int amount) {
        setType('D');
        setValues(new Integer[]{first, second, third, fourth});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (Arrays.asList(getValues()).contains(number)) {
            return getMoney() * (8 + 1);
        } else {
            return 0;
        }
    }
}
