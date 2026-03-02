package app.structures;

import app.structures.basic.Card;
import app.structures.basic.Deck;
import app.structures.basic.Hand;

/**
 * PlayerState extended for Sprint 2 draw support.
 */
public class PlayerState {
    private int currentMana;
    private final Hand hand = new Hand();
    private final Deck deck = new Deck();

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) { this.currentMana = Math.max(0, currentMana); }

    public Hand getHand() { return hand; }
    public Deck getDeck() { return deck; }

    public Card drawOne() {
        if (hand.size() >= Hand.MAX) return null;
        Card drawn = deck.drawTop();
        if (drawn == null) return null;
        boolean ok = hand.add(drawn);
        return ok ? drawn : null;
    }
}
