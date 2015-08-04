package bets;

/**
 * Created by eugen on 04.08.2015.
 */
public class BetG extends Bet {
    public BetG(int column, int amount) {
        setType('G');
        setValues(new Integer[]{column % 3});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (number>0 && number % 3 == getValues()[0]) {
            return getMoney() * (2 + 1);
        } else return 0;
    }
}
