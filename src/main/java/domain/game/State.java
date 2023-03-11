package domain.game;

import domain.card.Card;

import java.util.List;

public interface State {

    State draw(Card card);

    List<Card> cards();
}
