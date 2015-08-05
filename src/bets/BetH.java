package bets;

/**
 * Ставка типа H - дюжина
 */
public class BetH extends Bet {
    public BetH(int dozen, int amount) {
        setType('H');
        setValues(new Integer[]{dozen % 3});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (number>0 && (number-1) / 12 == getValues()[0]) {
            return getMoney() * (2 + 1);
        } else return 0;
    }
}
