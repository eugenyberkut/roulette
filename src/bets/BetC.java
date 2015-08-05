package bets;

import java.util.Arrays;

/**
 * Ставка типа С - стрит или 3 числа
 */
public class BetC extends Bet {
    public BetC(int first, int second, int third, int amount) {
        setType('C');
        setValues(new Integer[]{first, second, third});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (Arrays.asList(getValues()).contains(number)) {
            return getMoney() * (11 + 1);
        } else {
            return 0;
        }
    }
}
