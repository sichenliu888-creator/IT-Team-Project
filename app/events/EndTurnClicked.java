package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import commands.BasicCommands;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		// Execute all turn switching logic in the backend state machine
		gameState.switchTurn(out);

		//------
		// The following 4 lines are test code, only displayed in the terminal and do not affect game logic
        // if the terminal looks too messy, you can delete these 4 lines at any time
		System.out.println("End Turn Clicked! Now it is Player " + gameState.getCurrentTurn() + "'s turn.");
		System.out.println("Current Turn Number: " + gameState.getTurnNumber());
		System.out.println("Player 1 Mana: " + gameState.getPlayer1().getMana());
		System.out.println("Player 2 Mana: " + gameState.getPlayer2().getMana());
		//------

		// Send commands to the front-end browser to update the mana display on the UI
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());

		// Display a UI notification indicating whose turn it is
		if (gameState.getCurrentTurn() == 1) {
			BasicCommands.addPlayer1Notification(out, "Your Turn", 2);
		} else {
			// Assuming the front-end is the human player's view, show a notification for AI's turn
			BasicCommands.addPlayer1Notification(out, "AI's Turn", 2);
		}

		// Add a short sleep after sending all UI update commands to ensure front-end animations process in order
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
