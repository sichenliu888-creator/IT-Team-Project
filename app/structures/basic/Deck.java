package structures.basic;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Minimal deck implementation for Sprint 2 draw logic.
 */
public class Deck {
    private final Deque<Card> cards = new ArrayDeque<>();

    public void pushToTop(Card c) { if (c != null) cards.push(c); }
    public void addToBottom(Card c) { if (c != null) cards.addLast(c); }

    public Card drawTop() {
        return cards.pollFirst();
    }

    public int size() { return cards.size(); }
}
