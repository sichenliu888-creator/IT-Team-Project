package app.systems;

import app.abilities.Status;
import app.actions.PlayCardAction;
import app.effects.ErrorEffect;
import app.effects.GameEffect;
import app.effects.ManaChangeEffect;
import app.effects.StateSyncEffect;
import app.effects.SummonEffect;
import app.structures.GameState;
import app.structures.PlayerState;
import app.structures.basic.Card;
import app.structures.basic.Pos;
import app.structures.basic.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Systems are the ONLY layer mutating GameState.
 * - CardSystem handles spending mana, removing card, and placing unit on board.
 * - UI is NEVER called here; only effects returned.
 *
 * Edge cases enforced (Sprint 2 requirements):
 * - Card unaffordable -> no-op (ErrorEffect + no state change)
 * - Card not in hand -> no-op
 * - Invalid target tile -> no-op
 * - Insufficient mana -> no-op
 */
public final class CardSystem {

    private CardSystem() {}

    public static List<GameEffect> playCard(GameState state, PlayCardAction action) {
        List<GameEffect> effects = new ArrayList<>();

        if (state == null || action == null) {
            effects.add(new ErrorEffect("Invalid playCard call: null state/action"));
            return effects;
        }

        final int playerId = action.getPlayerId();
        final int handIndex = action.getHandIndex();
        final Pos target = action.getTargetPos();

        // Validate through Rules (no mutation)
        if (!Rules.canPlayCreatureAt(state, playerId, handIndex, target)) {
            effects.add(new ErrorEffect("Illegal creature summon"));
            return effects;
        }

        PlayerState ps = state.getPlayer(playerId);
        Card card = ps.getHand().get(handIndex);
        if (card == null) {
            effects.add(new ErrorEffect("Card not in hand"));
            return effects;
        }

        // Spend mana
        int newMana = ps.getCurrentMana() - card.getManaCost();
        if (newMana < 0) {
            effects.add(new ErrorEffect("Insufficient mana"));
            return effects;
        }
        ps.setCurrentMana(newMana);

        // Remove card from hand
        Card removed = ps.getHand().removeAt(handIndex);
        if (removed == null) {
            effects.add(new ErrorEffect("Card removal failed"));
            return effects;
        }

        // Create and place unit
        int unitId = state.allocateUnitId();
        Unit summoned = new Unit(unitId, playerId, removed.getAttack(), removed.getHealth(), target);

        // Apply summoning sickness (future-safe for Rush keyword):
        // - In Sprint 4, Rush can be implemented by not adding this status for RUSH units
        summoned.addStatus(Status.SUMMONING_SICKNESS);

        state.addUnit(summoned);

        // Clear selection after successful play (selection is UI-oriented state)
        state.setSelectedCardIndex(null);

        // Return effects
        effects.add(new ManaChangeEffect(playerId, ps.getCurrentMana()));
        effects.add(new SummonEffect(unitId, target));
        effects.add(new StateSyncEffect());

        return effects;
    }
}
