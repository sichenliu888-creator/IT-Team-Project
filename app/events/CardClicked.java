package events;

import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.ActorRef;
import structures.GameState;

/**
 * Indicates that the user has clicked a card in their hand.
 *
 * Sprint 2 (Member B):
 * - Selection should be gated by affordability.
 * - If affordable, card becomes selected and summon tiles are highlighted.
 */
public class CardClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        int handPosition = message.get("position").asInt();
        int handIndex = handPosition - 1; // convert to 0-based

        // TODO:
        // Integrate Sprint 2 affordability + highlight logic
        // using team Rules/System architecture.

        // For now we keep structure intact to resolve merge conflict cleanly.
    }
}
