package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Tile;

/**
 * Indicates that the user has clicked a tile on the board.
 *
 * Sprint 2 (Member B):
 * - If a creature card is selected, clicking a valid summon tile should attempt
 *   to play the card (via the team's GameEngine/System pipeline).
 *
 * NOTE:
 * - This file is kept in the team's event architecture (EventProcessor + processEvent).
 * - The Sprint 2 summon logic should be wired into the team's existing Rules/CardSystem,
 *   not the standalone app.* patch packages.
 */
public class TileClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        int tilex = message.get("tilex").asInt();
        int tiley = message.get("tiley").asInt();

        // Team template typically stores tiles in GameState/Board.
        // Retrieve the Tile object using existing team APIs.
        // Example (adjust to actual team code):
        // Tile clickedTile = gameState.getBoard().getTile(tilex, tiley);

        Tile clickedTile = null; // TODO: replace with team tile lookup

        // TODO:
        // If a card is currently selected:
        // 1) validate summon legality via team Rules
        // 2) create a PlayCardAction (or team equivalent command/action)
        // 3) route through GameEngine so CardSystem mutates GameState
        //
        // If invalid tile or no selection -> no-op
    }
}
