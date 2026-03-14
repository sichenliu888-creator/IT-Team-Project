package systems;

import actions.PlayCardAction;
import effects.GameEffect;
import structures.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles mana deduction, summoning and spell execution.
 * TODO Fix 3: implement using real structures.GameState API and structures.basic.Card.
 */
public final class CardSystem {

    private CardSystem() {}

    public static List<GameEffect> playCard(GameState state, PlayCardAction action) {
        // TODO Fix 3: implement creature summon and spell logic here.
        return new ArrayList<>();
    }
}
