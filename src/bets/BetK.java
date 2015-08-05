package bets;

/**
 * Ставка типа K - (большое-малое)
 */
public class BetK extends Bet {
    public BetK(int value, int amount) {
        setType('K');
        setMoney(amount);
        setValues(new Integer[]{value});
    }

    @Override
    public int win(int number) {
        if (number > 0 && ((getValues()[0]==0 && number<19)||(getValues()[0]==1 && number>18))) {
            return getMoney() * (1 + 1);
        } else return 0;
    }
}
