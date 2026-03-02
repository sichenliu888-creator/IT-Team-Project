package app.systems;

import app.abilities.Status;
import app.structures.GameState;
import app.structures.PlayerState;
import app.structures.basic.Board;
import app.structures.basic.Card;
import app.structures.basic.Hand;
import app.structures.basic.Pos;
import app.structures.basic.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ASSUMPTIONS:
 * - Rules is pure: validate/query only; never mutates GameState.
 * - Sprint 2 only implements creature card selection/placement rules.
 *
 * Summon rule (Story 12/13):
 * - Creature cards can be summoned on tiles adjacent (4-neighborhood) to ANY friendly unit.
 * - Target must be in bounds and unoccupied.
 */
public final class Rules {

    private Rules() {}

    public static boolean isCurrentPlayersTurn(GameState state, int playerId) {
        return state.getCurrentPlayerId() == playerId;
    }

    public static boolean canSelectCard(GameState state, int playerId, int handIndex) {
        if (!isCurrentPlayersTurn(state, playerId)) return false;

        PlayerState ps = state.getPlayer(playerId);
        Card card = ps.getHand().get(handIndex);
        if (card == null) return false;

        // Story 12: clicking unaffordable card does nothing
        return card.getManaCost() <= ps.getCurrentMana();
    }

    public static List<Pos> getValidSummonTiles(GameState state, int playerId) {
        Board board = state.getBoard();
        Set<Pos> unique = new HashSet<>();

        for (Unit u : state.getUnits().values()) {
            if (u.getOwnerId() != playerId) continue;

            // Adjacent 4-neighborhood
            Pos base = u.getPos();
            if (base == null) continue;

            addIfSummonable(board, unique, base.add(1, 0));
            addIfSummonable(board, unique, base.add(-1, 0));
            addIfSummonable(board, unique, base.add(0, 1));
            addIfSummonable(board, unique, base.add(0, -1));
        }

        return new ArrayList<>(unique);
    }

    private static void addIfSummonable(Board board, Set<Pos> out, Pos p) {
        if (!board.inBounds(p)) return;
        if (board.isOccupied(p)) return;
        out.add(p);
    }

    public static boolean canPlayCreatureAt(GameState state, int playerId, int handIndex, Pos target) {
        if (!isCurrentPlayersTurn(state, playerId)) return false;

        PlayerState ps = state.getPlayer(playerId);
        Hand hand = ps.getHand();

        Card card = hand.get(handIndex);
        if (card == null) return false;

        if (card.getType() != Card.Type.CREATURE) return false;
        if (card.getManaCost() > ps.getCurrentMana()) return false;

        // target must be a valid summon tile
        if (target == null) return false;
        if (!state.getBoard().inBounds(target)) return false;
        if (state.getBoard().isOccupied(target)) return false;

        // must be in valid set computed from friendly adjacency
        List<Pos> valid = getValidSummonTiles(state, playerId);
        return valid.contains(target);
    }

    /**
     * Sprint-safety hook:
     * - used by movement/combat in future: units can't act when summoning sickness is present
     * - Rush later can bypass by removing the status on summon
     */
    public static boolean canUnitActThisTurn(Unit u) {
        if (u == null) return false;
        return !u.hasStatus(Status.SUMMONING_SICKNESS) && !u.hasStatus(Status.STUNNED);
    }
}
