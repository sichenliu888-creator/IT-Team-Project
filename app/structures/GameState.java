package structures;
import java.util.ArrayList;
import java.util.List;

import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {

	
	public boolean gameInitalised = false;
	
	public boolean something = false;
	
	// board
    private Tile[][] board = new Tile[10][6];           // Visual tiles
    private GameUnit[][] unitBoard = new GameUnit[10][6]; // Units on tiles

    // players
    private Player player1;
    private Player player2;
    private GameUnit player1Avatar;
    private GameUnit player2Avatar;

    // decks and hands
    private List<Card> player1Deck = new ArrayList<>();
    private List<Card> player2Deck = new ArrayList<>();
    private List<Card> player1Hand = new ArrayList<>();
    private List<Card> player2Hand = new ArrayList<>();

    // turn tracking
    private int currentTurn = 1;    // 1 is current player (player 1) turn, 2 = player2's turn
    private int turnNumber = 1;     // Increments each full round

    // UI selection state
    private GameUnit selectedUnit;
    private Card selectedCard;
    private int selectedCardHandPosition = -1;
    private List<Tile> highlightedTiles = new ArrayList<>();
    private List<Tile> attackHighlightedTiles = new ArrayList<>();

    // unit id count
    private int nextUnitId = 0;

    // animation lock
    private boolean unitMoving = false;

    // game over
    private boolean gameOver = false;

    // board methods
    public Tile getTile(int x, int y) {
        if (x >= 1 && x <= 9 && y >= 1 && y <= 5) {
            return board[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 1 && x <= 9 && y >= 1 && y <= 5) {
            board[x][y] = tile;
        }
    }

    public GameUnit getUnitOnTile(int x, int y) {
        if (x >= 1 && x <= 9 && y >= 1 && y <= 5) {
            return unitBoard[x][y];
        }
        return null;
    }

    public void placeUnit(int x, int y, GameUnit unit) {
        if (x >= 1 && x <= 9 && y >= 1 && y <= 5) {
            unitBoard[x][y] = unit;
            unit.setPosition(x, y);
        }
    }

    public void removeUnit(int x, int y) {
        if (x >= 1 && x <= 9 && y >= 1 && y <= 5) {
            unitBoard[x][y] = null;
        }
    }

    public void moveUnit(GameUnit unit, int toX, int toY) {
        // Remove from old position
        removeUnit(unit.getTileX(), unit.getTileY());
        // Place at new position
        placeUnit(toX, toY, unit);
    }

    // unit id
    public int getAndIncrementUnitId() {
        return nextUnitId++;
    }

    // selection methods
    public GameUnit getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(GameUnit unit) { this.selectedUnit = unit; }

    public Card getSelectedCard() { return selectedCard; }
    public void setSelectedCard(Card card) { this.selectedCard = card; }

    public int getSelectedCardHandPosition() { return selectedCardHandPosition; }
    public void setSelectedCardHandPosition(int pos) { this.selectedCardHandPosition = pos; }

    public void clearSelection() {
        this.selectedUnit = null;
        this.selectedCard = null;
        this.selectedCardHandPosition = -1;
    }

    public boolean hasSelection() {
        return selectedUnit != null || selectedCard != null;
    }

    // highlight methods
    public List<Tile> getHighlightedTiles() { return highlightedTiles; }
    public void setHighlightedTiles(List<Tile> tiles) { this.highlightedTiles = tiles; }

    public List<Tile> getAttackHighlightedTiles() { return attackHighlightedTiles; }
    public void setAttackHighlightedTiles(List<Tile> tiles) { this.attackHighlightedTiles = tiles; }

    // player methods
    public Player getPlayer1() { return player1; }
    public void setPlayer1(Player player1) { this.player1 = player1; }

    public Player getPlayer2() { return player2; }
    public void setPlayer2(Player player2) { this.player2 = player2; }

    public GameUnit getPlayer1Avatar() { return player1Avatar; }
    public void setPlayer1Avatar(GameUnit avatar) { this.player1Avatar = avatar; }

    public GameUnit getPlayer2Avatar() { return player2Avatar; }
    public void setPlayer2Avatar(GameUnit avatar) { this.player2Avatar = avatar; }

    // deck methods
    public List<Card> getPlayer1Deck() { return player1Deck; }
    public void setPlayer1Deck(List<Card> deck) { this.player1Deck = deck; }

    public List<Card> getPlayer2Deck() { return player2Deck; }
    public void setPlayer2Deck(List<Card> deck) { this.player2Deck = deck; }

    // hand methods
    public List<Card> getPlayer1Hand() { return player1Hand; }
    public void setPlayer1Hand(List<Card> hand) { this.player1Hand = hand; }

    public List<Card> getPlayer2Hand() { return player2Hand; }
    public void setPlayer2Hand(List<Card> hand) { this.player2Hand = hand; }

    // turn methods
    public int getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(int turn) { this.currentTurn = turn; }

    public int getTurnNumber() { return turnNumber; }
    public void setTurnNumber(int turnNumber) { this.turnNumber = turnNumber; }

    // amination lock
    public boolean isUnitMoving() { return unitMoving; }
    public void setUnitMoving(boolean moving) { this.unitMoving = moving; }

    // game over
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

}
