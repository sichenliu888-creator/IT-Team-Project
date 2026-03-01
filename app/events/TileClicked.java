package events;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.GameUnit;
import structures.basic.Tile;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 *
 * {
 *   messageType = "tileClicked"
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 *
 * Sprint 1 (Member B): unit selection + movement highlight + execute movement
 *
 * @author Dr. Richard McCreadie
 */
public class TileClicked implements EventProcessor {

    // Tile draw modes: 0 = normal. In most templates 1 = highlight.
    // If your UI highlight doesn't show, try changing HIGHLIGHT to 2.
    private static final int NORMAL = 0;
    private static final int HIGHLIGHT = 1;

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (!gameState.gameInitalised) return;
        if (gameState.isGameOver()) return;

        // ignore clicks while movement animation is playing
        if (gameState.isUnitMoving()) return;

        int tilex = message.get("tilex").asInt(); // 1-based
        int tiley = message.get("tiley").asInt(); // 1-based

        Tile clickedTile = gameState.getTile(tilex, tiley);
        if (clickedTile == null) return;

        // A) If clicked tile is highlighted and a unit is selected -> move the selected unit
        if (isHighlighted(gameState, clickedTile) && gameState.getSelectedUnit() != null) {

            GameUnit selected = gameState.getSelectedUnit();

            // cannot move onto occupied tile
            if (gameState.getUnitOnTile(tilex, tiley) != null) return;

            // lock input until unitStopped event arrives
            gameState.setUnitMoving(true);

            // animate unit movement in UI
            BasicCommands.moveUnitToTile(out, selected.getUnit(), clickedTile);

            // update backend board state
            gameState.moveUnit(selected, tilex, tiley);
            selected.setHasMoved(true);

            // clear highlights and selection
            clearMoveHighlights(out, gameState);
            gameState.setSelectedUnit(null);

            return;
        }

        // B) Otherwise, if clicked a friendly unit (owner=1) during human turn -> select & highlight moves
        GameUnit unitOnTile = gameState.getUnitOnTile(tilex, tiley);
        if (unitOnTile != null && unitOnTile.getOwner() == 1 && gameState.getCurrentTurn() == 1) {

            // clear old highlights then select
            clearMoveHighlights(out, gameState);
            gameState.setSelectedUnit(unitOnTile);

            // (optional) if already moved this turn, don't highlight moves
            if (unitOnTile.hasMoved()) return;

            List<Tile> validMoves = computeValidMoveTiles(gameState, unitOnTile);

            for (Tile t : validMoves) {
                BasicCommands.drawTile(out, t, HIGHLIGHT);
            }
            gameState.setHighlightedTiles(validMoves);

            return;
        }

        // C) Otherwise -> clear selection/highlights
        clearMoveHighlights(out, gameState);
        gameState.setSelectedUnit(null);
    }

    private boolean isHighlighted(GameState gameState, Tile tile) {
        for (Tile t : gameState.getHighlightedTiles()) {
            if (t.getTilex() == tile.getTilex() && t.getTiley() == tile.getTiley()) {
                return true;
            }
        }
        return false;
    }

    private void clearMoveHighlights(ActorRef out, GameState gameState) {
        for (Tile t : gameState.getHighlightedTiles()) {
            BasicCommands.drawTile(out, t, NORMAL);
        }
        gameState.getHighlightedTiles().clear();
    }

    /**
     * Minimal movement rule for Sprint 1 demo:
     * - 2 steps orthogonal (N/S/E/W)
     * - 1 step diagonal
     * - can't move out of bounds
     * - can't move onto occupied tiles
     */
	private List<Tile> computeValidMoveTiles(GameState gameState, GameUnit unit) {

		List<Tile> result = new ArrayList<>();

		int ux = unit.getTileX();
		int uy = unit.getTileY();

		// ---------- 1 STEP CARDINAL ----------
		int[][] oneStepCardinal = {
			{ 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
		};

		for (int[] d : oneStepCardinal) {
			int nx = ux + d[0];
			int ny = uy + d[1];

			Tile t = gameState.getTile(nx, ny);
			if (t == null) continue;
			if (gameState.getUnitOnTile(nx, ny) != null) continue;

			result.add(t);
		}

		// ---------- 2 STEP CARDINAL (CHECK BLOCKING) ----------
		int[][] twoStepCardinal = {
			{ 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 }
		};

		for (int[] d : twoStepCardinal) {

			int nx = ux + d[0];
			int ny = uy + d[1];

			Tile destination = gameState.getTile(nx, ny);
			if (destination == null) continue;

			// check middle tile
			int midX = ux + d[0] / 2;
			int midY = uy + d[1] / 2;

			if (gameState.getUnitOnTile(midX, midY) != null) continue;

			if (gameState.getUnitOnTile(nx, ny) != null) continue;

			result.add(destination);
		}

		// ---------- 1 STEP DIAGONAL ----------
		int[][] diagonal = {
			{ 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
		};

		for (int[] d : diagonal) {

			int nx = ux + d[0];
			int ny = uy + d[1];

			Tile t = gameState.getTile(nx, ny);
			if (t == null) continue;
			if (gameState.getUnitOnTile(nx, ny) != null) continue;

			result.add(t);
		}

		return result;
	}
}