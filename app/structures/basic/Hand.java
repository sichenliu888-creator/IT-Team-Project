package app.structures.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Hand capacity is 6 (per doc).
 * - Cards addressed by index in-hand (0..size-1).
 */
public class Hand {
    public static final int MAX = 6;
    private final List<Card> cards = new ArrayList<>();

    public List<Card> getCardsView() {
        return Collections.unmodifiableList(cards);
    }

    public int size() { return cards.size(); }

    public Card get(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.get(index);
    }

    public boolean contains(Card c) {
        return cards.contains(c);
    }

    public boolean add(Card c) {
        if (cards.size() >= MAX) return false;
        cards.add(c);
        return true;
    }

    public Card removeAt(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.remove(index);
    }
}
