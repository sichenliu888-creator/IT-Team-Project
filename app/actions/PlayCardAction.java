package actions;

import structures.Pos;

/**
 * ASSUMPTIONS:
 * - PlayCardAction supports a card index + optional target.
 * - For Sprint 2: targetPos is required for creature summon; spells not implemented.
 */
public final class PlayCardAction implements GameAction {
    private final int playerId;
    private final int handIndex;
    private final Pos targetPos; // nullable for some future card types

    public PlayCardAction(int playerId, int handIndex, Pos targetPos) {
        this.playerId = playerId;
        this.handIndex = handIndex;
        this.targetPos = targetPos;
    }

    public int getPlayerId() { return playerId; }
    public int getHandIndex() { return handIndex; }
    public Pos getTargetPos() { return targetPos; }
}
