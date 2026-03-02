package app.systems;

import app.actions.GameAction;
import app.actions.PlayCardAction;
import app.effects.GameEffect;
import app.structures.GameState;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - GameEngine is the ONLY entry point for applying actions (doc).
 * - Validation is in Rules; mutation in Systems.
 * - Sprint 2 only routes PlayCardAction to CardSystem.
 */
public final class GameEngine {

    private GameEngine() {}

    public static List<GameEffect> apply(GameState state, GameAction action) {
        if (state == null || action == null) return Collections.emptyList();

        if (action instanceof PlayCardAction) {
            return CardSystem.playCard(state, (PlayCardAction) action);
        }

        // Sprint 2 scope: ignore other actions
        return Collections.emptyList();
    }
}
