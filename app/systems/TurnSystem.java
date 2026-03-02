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
