package app.events;

import app.effects.GameEffect;
import app.effects.HighlightEffect;
import app.structures.GameState;
import app.systems.Rules;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Event handler creates NO actions; it may return highlight effects directly
 *   (or in your real repo, it delegates to HumanController).
 * - Story 12 requires:
 *   * Clicking affordable card selects it
 *   * Highlights valid summon tiles for creature cards
 *   * Clicking unaffordable card does nothing
 */
public final class CardClicked {

    private CardClicked() {}

    public static List<GameEffect> onCardClicked(GameState state, int playerId, int handIndex) {
        if (state == null) return Collections.emptyList();

        if (!Rules.canSelectCard(state, playerId, handIndex)) {
            // no-op: clicking unaffordable card does nothing
            return Collections.emptyList();
        }

        // Store selection (could be controller-level in your real repo)
        state.setSelectedCardIndex(handIndex);

        // Highlight valid creature summon tiles (Sprint 2 only)
        List<?> tiles = Rules.getValidSummonTiles(state, playerId);
        @SuppressWarnings("unchecked")
        List<app.structures.basic.Pos> positions = (List<app.structures.basic.Pos>) tiles;

        return List.of(new HighlightEffect(positions, HighlightEffect.Mode.SUMMON));
    }
}
