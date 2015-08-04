package bets;

/**
 * Created by eugen on 04.08.2015.
 */
public class BetA extends Bet {

    public BetA(int number, int amount) {
        setType('A');
        setValues(new Integer[]{number});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (number==getValues()[0]) {
            return getMoney()*(35+1);
        } else {
            return 0;
        }
    }
}
