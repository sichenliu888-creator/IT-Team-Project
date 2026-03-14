package systems;

import actions.EndTurnAction;
import effects.GameEffect;
import structures.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles turn switching, mana refresh, card draw and state reset.
 * TODO Fix 3: implement using real structures.GameState API.
 */
public final class TurnSystem {

    private TurnSystem() {}

    public static List<GameEffect> endTurn(GameState state, EndTurnAction action) {
        // TODO Fix 3: implement full turn logic here using structures.GameState API.
        // For now the working turn logic lives in GameState.switchTurn(out) called by EndTurnClicked.
        return new ArrayList<>();
    }
}
