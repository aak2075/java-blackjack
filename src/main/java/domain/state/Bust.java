package domain.state;

import domain.user.Hand;

public class Bust extends Finished {

    private static final int PROFIT_RATE = -1;

    Bust(Hand hand) {
        super(hand);
    }

    @Override
    double profitRate() {
        return PROFIT_RATE;
    }

    @Override
    public boolean isStay() {
        return false;
    }
}
