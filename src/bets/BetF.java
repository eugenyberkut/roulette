package bets;

/**
 * Created by eugen on 04.08.2015.
 */
public class BetF extends Bet {
    public BetF(int first, int second, int amount) {
        setType('F');
        setMoney(amount);
        setValues(new Integer[]{first,second});
    }

    @Override
    public int win(int number) {
        if (number >= getValues()[0] && number <= getValues()[1]) {
            return getMoney() * (5 + 1);
        } else return 0;
    }
}
