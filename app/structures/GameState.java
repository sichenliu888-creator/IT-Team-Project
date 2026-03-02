package app.structures;

import app.structures.basic.Board;
import app.structures.basic.Pos;
import app.structures.basic.Unit;

import java.util.HashMap;
import java.util.Map;

/**
 * ASSUMPTIONS:
 * - GameState stores: board, players, units, turn ownership, and selected card index for UI flow.
 * - Real repo may store selection in HumanController instead. If so, move selectedCardIndex there.
 */
public class GameState {
    private final Board board = new Board();
    private final PlayerState[] players = new PlayerState[] { new PlayerState(), new PlayerState() };

    private int currentPlayerId = 1; // 1 = human, 2 = AI (per doc conventions)
    private int turnNumber = 0;

    // Selection state used by CardClicked/TileClicked flow (Story 12/13)
    private Integer selectedCardIndex = null;

    // Units by id
    private final Map<Integer, Unit> units = new HashMap<>();
    private int nextUnitId = 1000;

    public Board getBoard() { return board; }

    public PlayerState getPlayer(int playerId) {
        return players[playerId - 1];
    }

    public int getCurrentPlayerId() { return currentPlayerId; }
    public void setCurrentPlayerId(int currentPlayerId) { this.currentPlayerId = currentPlayerId; }

    public int getTurnNumber() { return turnNumber; }
    public void setTurnNumber(int turnNumber) { this.turnNumber = turnNumber; }

    public Integer getSelectedCardIndex() { return selectedCardIndex; }
    public void setSelectedCardIndex(Integer selectedCardIndex) { this.selectedCardIndex = selectedCardIndex; }

    public Map<Integer, Unit> getUnits() { return units; }

    public Unit getUnit(int unitId) { return units.get(unitId); }

    public int allocateUnitId() { return nextUnitId++; }

    public void addUnit(Unit u) {
        units.put(u.getId(), u);
        board.setUnitAt(u.getPos(), u.getId());
    }

    public void moveUnit(int unitId, Pos to) {
        Unit u = units.get(unitId);
        if (u == null) return;
        board.setUnitAt(u.getPos(), null);
        u.setPos(to);
        board.setUnitAt(to, unitId);
    }
}
