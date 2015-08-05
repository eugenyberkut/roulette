package bets;

/**
 * Ставка типа F - два ряда (6 цифр)
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
