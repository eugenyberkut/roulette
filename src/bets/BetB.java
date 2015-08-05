package bets;

import java.util.Arrays;

/**
 * Ставка типа B - два числа
 */
public class BetB extends Bet {
    public BetB(int first, int second, int amount) {
        setType('B');
        setValues(new Integer[]{first, second});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (Arrays.asList(getValues()).contains(number)) {
            return getMoney()*(17+1);
        } else {
            return 0;
        }
    }
}
