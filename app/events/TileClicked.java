package app.events;

import app.actions.PlayCardAction;
import app.effects.GameEffect;
import app.structures.GameState;
import app.structures.basic.Pos;
import app.systems.GameEngine;
import app.systems.Rules;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Clicking a tile with a selected creature card attempts to play it.
 * - Invalid tile -> rejected safely (no-op or ErrorEffect from CardSystem).
 * - No valid summon tiles -> highlight effect will be empty; this click will be illegal.
 */
public final class TileClicked {

    private TileClicked() {}

    public static List<GameEffect> onTileClicked(GameState state, int playerId, Pos target) {
        if (state == null || target == null) return Collections.emptyList();

        Integer selected = state.getSelectedCardIndex();
        if (selected == null) return Collections.emptyList();

        // Validate target before creating action (keeps controller logic simple; Rules is pure)
        if (!Rules.canPlayCreatureAt(state, playerId, selected, target)) {
            return Collections.emptyList();
        }

        return GameEngine.apply(state, new PlayCardAction(playerId, selected, target));
    }
}
