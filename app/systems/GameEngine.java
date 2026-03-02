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
