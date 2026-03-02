package app.structures;

import app.structures.basic.Hand;

/**
 * ASSUMPTIONS:
 * - PlayerState holds current mana and hand only (minimal for Sprint 2).
 * - TurnSystem (Member A) owns refresh; CardSystem spends mana.
 */
public class PlayerState {
    private int currentMana;
    private final Hand hand = new Hand();

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) { this.currentMana = Math.max(0, currentMana); }

    public Hand getHand() { return hand; }
}
