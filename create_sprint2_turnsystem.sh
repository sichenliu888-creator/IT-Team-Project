#!/usr/bin/env bash
set -euo pipefail

echo "Creating Sprint 2 Commit 5–7 files..."

# -----------------------------
# COMMIT 5 FILES
# -----------------------------

mkdir -p app/actions
cat > app/actions/EndTurnAction.java <<'EOF'
package app.actions;

/**
 * ASSUMPTIONS:
 * - EndTurnAction is a simple intent created by controllers.
 * - Sprint 2 needs end-turn to trigger mana refresh + draw.
 */
public final class EndTurnAction implements GameAction {
    private final int playerId;

    public EndTurnAction(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() { return playerId; }
}
EOF

mkdir -p app/effects
cat > app/effects/DrawCardEffect.java <<'EOF'
package app.effects;

/**
 * ASSUMPTIONS:
 * - UI can respond to a draw by refreshing hand display.
 */
public final class DrawCardEffect implements GameEffect {
    private final int playerId;
    private final int newHandSize;

    public DrawCardEffect(int playerId, int newHandSize) {
        this.playerId = playerId;
        this.newHandSize = newHandSize;
    }

    public int getPlayerId() { return playerId; }
    public int getNewHandSize() { return newHandSize; }
}
EOF

git add app/actions/EndTurnAction.java app/effects/DrawCardEffect.java
git commit -m "Sprint2: add EndTurnAction and DrawCardEffect"

# -----------------------------
# COMMIT 6 FILES
# -----------------------------

mkdir -p app/structures/basic
cat > app/structures/basic/Deck.java <<'EOF'
package app.structures.basic;

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
EOF

cat > app/structures/PlayerState.java <<'EOF'
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
EOF

mkdir -p app/systems
cat > app/systems/TurnSystem.java <<'EOF'
package app.systems;

import app.abilities.Status;
import app.actions.EndTurnAction;
import app.effects.DrawCardEffect;
import app.effects.ErrorEffect;
import app.effects.GameEffect;
import app.effects.ManaChangeEffect;
import app.effects.StateSyncEffect;
import app.structures.GameState;
import app.structures.PlayerState;
import app.structures.basic.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Sprint 2 TurnSystem:
 * - Switch player
 * - Increment turnNumber after player 2
 * - Refresh mana
 * - Clear summoning sickness
 * - Draw 1 card
 */
public final class TurnSystem {

    private TurnSystem() {}

    public static List<GameEffect> endTurn(GameState state, EndTurnAction action) {
        List<GameEffect> effects = new ArrayList<>();

        if (state == null || action == null) {
            effects.add(new ErrorEffect("Invalid endTurn call"));
            return effects;
        }

        int playerId = action.getPlayerId();
        if (state.getCurrentPlayerId() != playerId) {
            effects.add(new ErrorEffect("Not your turn"));
            return effects;
        }

        int nextPlayer = (playerId == 1) ? 2 : 1;

        if (playerId == 2) {
            state.setTurnNumber(state.getTurnNumber() + 1);
        }

        state.setCurrentPlayerId(nextPlayer);
        state.setSelectedCardIndex(null);

        int refreshed = Math.min(state.getTurnNumber() + 1, 9);
        PlayerState ps = state.getPlayer(nextPlayer);
        ps.setCurrentMana(refreshed);
        effects.add(new ManaChangeEffect(nextPlayer, ps.getCurrentMana()));

        for (Unit u : state.getUnits().values()) {
            if (u.getOwnerId() == nextPlayer) {
                u.removeStatus(Status.SUMMONING_SICKNESS);
            }
        }

        ps.drawOne();
        effects.add(new DrawCardEffect(nextPlayer, ps.getHand().size()));

        effects.add(new StateSyncEffect());
        return effects;
    }
}
EOF

git add app/structures/basic/Deck.java app/structures/PlayerState.java app/systems/TurnSystem.java
git commit -m "Sprint2: implement TurnSystem (mana refresh, draw, clear sickness)"

# -----------------------------
# COMMIT 7 FILE MODIFICATION
# -----------------------------

cat > app/systems/GameEngine.java <<'EOF'
package app.systems;

import app.actions.EndTurnAction;
import app.actions.GameAction;
import app.actions.PlayCardAction;
import app.effects.GameEffect;
import app.structures.GameState;

import java.util.Collections;
import java.util.List;

/**
 * GameEngine routes actions to systems.
 */
public final class GameEngine {

    private GameEngine() {}

    public static List<GameEffect> apply(GameState state, GameAction action) {
        if (state == null || action == null) return Collections.emptyList();

        if (action instanceof PlayCardAction) {
            return CardSystem.playCard(state, (PlayCardAction) action);
        }

        if (action instanceof EndTurnAction) {
            return TurnSystem.endTurn(state, (EndTurnAction) action);
        }

        return Collections.emptyList();
    }
}
EOF

git add app/systems/GameEngine.java
git commit -m "Sprint2: route EndTurnAction via GameEngine"

echo "✅ Commits 5–7 created successfully."
echo "Run compile check:"
echo "  find app -name \"*.java\" > sources.txt"
echo "  javac @sources.txt"
