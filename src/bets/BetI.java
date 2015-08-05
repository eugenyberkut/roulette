package bets;

/**
 * Ставка типа I - Красное/Черное
 */
public class BetI extends Bet {
    public BetI(int value, int amount) {
        setType('I');
        setValues(new Integer[]{value});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (number > 0 && (REDS.contains(number) && getValues()[0]==RED) || (!REDS.contains(number) && getValues()[0]==BLACK)) {
            return getMoney() * (1 + 1);
        } else return 0;
    }
}
