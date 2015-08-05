package bets;

/**
 * Ставка типа J - чет-нечет
 */
public class BetJ extends Bet {
    public BetJ(int value, int amount) {
        setType('J');
        setValues(new Integer[]{value});
        setMoney(amount);
    }

    @Override
    public int win(int number) {
        if (number > 0 && (number + getValues()[0])%2==0 ) {
            return getMoney() * (1 + 1);
        } else return 0;
    }
}
