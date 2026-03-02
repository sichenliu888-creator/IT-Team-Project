#!/usr/bin/env bash
set -euo pipefail

# Creates the Sprint 2 patch file tree under ./app
# WARNING: Overwrites existing files with same paths.

write_file() {
  local path="$1"
  shift
  mkdir -p "$(dirname "$path")"
  cat > "$path" <<'EOF'
'"$@"'
EOF
}

# --- app/abilities/Status.java ---
mkdir -p app/abilities
cat > app/abilities/Status.java <<'EOF'
package app.abilities;

/**
 * ASSUMPTIONS (Sprint2 patch):
 * - Status exists and is checked by Rules to prevent acting.
 * - We only need SUMMONING_SICKNESS for Sprint 2; Rush later can bypass.
 */
public enum Status {
    SUMMONING_SICKNESS,
    STUNNED
}
EOF

# --- app/abilities/Keyword.java ---
cat > app/abilities/Keyword.java <<'EOF'
package app.abilities;

/**
 * ASSUMPTIONS:
 * - Keyword enum exists for future sprints.
 * - Sprint 2 does NOT implement keyword rules; we include RUSH only so future
 *   bypass logic is non-breaking.
 */
public enum Keyword {
    RUSH,
    FLYING,
    PROVOKE
}
EOF

# --- app/structures/basic/Pos.java ---
mkdir -p app/structures/basic
cat > app/structures/basic/Pos.java <<'EOF'
package app.structures.basic;

import java.util.Objects;

/**
 * ASSUMPTIONS:
 * - Board is 9x5, 1-based or 0-based indexing unknown in the real repo.
 * - This patch uses 0-based (x in [0..8], y in [0..4]) for simplicity.
 *   If your real code is 1-based, adjust Board.inBounds() and adjacency generation only.
 */
public final class Pos {
    public final int x;
    public final int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos add(int dx, int dy) {
        return new Pos(this.x + dx, this.y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pos)) return false;
        Pos other = (Pos) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pos(" + x + "," + y + ")";
    }
}
EOF

# --- app/structures/basic/Board.java ---
cat > app/structures/basic/Board.java <<'EOF'
package app.structures.basic;

import java.util.HashMap;
import java.util.Map;

/**
 * ASSUMPTIONS:
 * - Board tracks occupancy by unitId at a Pos.
 * - Real repo may store 2D arrays; adapt getUnitIdAt/setUnitAt accordingly.
 */
public class Board {
    public static final int WIDTH = 9;
    public static final int HEIGHT = 5;

    private final Map<Pos, Integer> posToUnitId = new HashMap<>();

    public boolean inBounds(Pos p) {
        return p != null && p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT;
    }

    public Integer getUnitIdAt(Pos p) {
        return posToUnitId.get(p);
    }

    public boolean isOccupied(Pos p) {
        return getUnitIdAt(p) != null;
    }

    public void setUnitAt(Pos p, Integer unitId) {
        if (unitId == null) {
            posToUnitId.remove(p);
        } else {
            posToUnitId.put(p, unitId);
        }
    }
}
EOF

# --- app/structures/basic/Card.java ---
cat > app/structures/basic/Card.java <<'EOF'
package app.structures.basic;

/**
 * ASSUMPTIONS:
 * - Sprint 2 only needs creature card cost/attack/health.
 * - Spell targeting is NOT implemented (Sprint 3).
 */
public class Card {
    public enum Type { CREATURE, SPELL }

    private final String name;
    private final int manaCost;
    private final Type type;
    private final int attack;
    private final int health;

    public Card(String name, int manaCost, Type type, int attack, int health) {
        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
        this.attack = attack;
        this.health = health;
    }

    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public Type getType() { return type; }

    // Creature stats (meaningful only if type == CREATURE)
    public int getAttack() { return attack; }
    public int getHealth() { return health; }
}
EOF

# --- app/structures/basic/Hand.java ---
cat > app/structures/basic/Hand.java <<'EOF'
package app.structures.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Hand capacity is 6 (per doc).
 * - Cards addressed by index in-hand (0..size-1).
 */
public class Hand {
    public static final int MAX = 6;
    private final List<Card> cards = new ArrayList<>();

    public List<Card> getCardsView() {
        return Collections.unmodifiableList(cards);
    }

    public int size() { return cards.size(); }

    public Card get(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.get(index);
    }

    public boolean contains(Card c) {
        return cards.contains(c);
    }

    public boolean add(Card c) {
        if (cards.size() >= MAX) return false;
        cards.add(c);
        return true;
    }

    public Card removeAt(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.remove(index);
    }
}
EOF

# --- app/structures/basic/Unit.java ---
cat > app/structures/basic/Unit.java <<'EOF'
package app.structures.basic;

import app.abilities.Status;

import java.util.EnumSet;
import java.util.Set;

/**
 * ASSUMPTIONS:
 * - Unit is runtime state on board.
 * - Sprint 2 needs: owner, stats, position, and statuses including SUMMONING_SICKNESS.
 * - "Cannot act this turn" is represented by SUMMONING_SICKNESS; future Rush bypasses it.
 */
public class Unit {
    private final int id;
    private final int ownerId;
    private int attack;
    private int health;
    private Pos pos;

    private final Set<Status> statuses = EnumSet.noneOf(Status.class);

    public Unit(int id, int ownerId, int attack, int health, Pos pos) {
        this.id = id;
        this.ownerId = ownerId;
        this.attack = attack;
        this.health = health;
        this.pos = pos;
    }

    public int getId() { return id; }
    public int getOwnerId() { return ownerId; }
    public int getAttack() { return attack; }
    public int getHealth() { return health; }
    public Pos getPos() { return pos; }

    public void setPos(Pos pos) { this.pos = pos; }

    public Set<Status> getStatuses() { return statuses; }
    public boolean hasStatus(Status s) { return statuses.contains(s); }
    public void addStatus(Status s) { statuses.add(s); }
    public void removeStatus(Status s) { statuses.remove(s); }
}
EOF

# --- app/structures/PlayerState.java ---
mkdir -p app/structures
cat > app/structures/PlayerState.java <<'EOF'
package app.structures;

import app.structures.basic.Hand;

/**
 * ASSUMPTIONS:
 * - PlayerState holds current mana and hand only (minimal for Sprint 2).
 * - TurnSystem (Member A) owns refresh; CardSystem spends mana.
 */
public class PlayerState {
    private int currentMana;
    private final Hand hand = new Hand();

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) { this.currentMana = Math.max(0, currentMana); }

    public Hand getHand() { return hand; }
}
EOF

# --- app/structures/GameState.java ---
cat > app/structures/GameState.java <<'EOF'
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
EOF

# --- app/systems/Rules.java ---
mkdir -p app/systems
cat > app/systems/Rules.java <<'EOF'
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
EOF

# --- app/actions/GameAction.java ---
mkdir -p app/actions
cat > app/actions/GameAction.java <<'EOF'
package app.actions;

/**
 * ASSUMPTIONS:
 * - Marker interface for all intents (doc).
 */
public interface GameAction {}
EOF

# --- app/actions/PlayCardAction.java ---
cat > app/actions/PlayCardAction.java <<'EOF'
package app.actions;

import app.structures.basic.Pos;

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
EOF

# --- app/effects/GameEffect.java ---
mkdir -p app/effects
cat > app/effects/GameEffect.java <<'EOF'
package app.effects;

/**
 * ASSUMPTIONS:
 * - Marker interface for engine outputs.
 */
public interface GameEffect {}
EOF

# --- app/effects/ErrorEffect.java ---
cat > app/effects/ErrorEffect.java <<'EOF'
package app.effects;

/**
 * ASSUMPTIONS:
 * - Used to represent invalid action feedback without mutating state.
 * - UI may ignore this in your real repo; still useful for tests/logging.
 */
public final class ErrorEffect implements GameEffect {
    private final String message;

    public ErrorEffect(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
EOF

# --- app/effects/StateSyncEffect.java ---
cat > app/effects/StateSyncEffect.java <<'EOF'
package app.effects;

/**
 * ASSUMPTIONS:
 * - UI can refresh full state from GameState when this effect appears.
 */
public final class StateSyncEffect implements GameEffect {}
EOF

# --- app/effects/ManaChangeEffect.java ---
cat > app/effects/ManaChangeEffect.java <<'EOF'
package app.effects;

/**
 * ASSUMPTIONS:
 * - Sprint 2 needs mana updates to display after spending.
 */
public final class ManaChangeEffect implements GameEffect {
    private final int playerId;
    private final int newMana;

    public ManaChangeEffect(int playerId, int newMana) {
        this.playerId = playerId;
        this.newMana = newMana;
    }

    public int getPlayerId() { return playerId; }
    public int getNewMana() { return newMana; }
}
EOF

# --- app/effects/SummonEffect.java ---
cat > app/effects/SummonEffect.java <<'EOF'
package app.effects;

import app.structures.basic.Pos;

/**
 * ASSUMPTIONS:
 * - Sprint 2 summon should produce a SummonEffect for UI animation.
 * - We include unitId + position; UI can look up stats from GameState.
 */
public final class SummonEffect implements GameEffect {
    private final int unitId;
    private final Pos pos;

    public SummonEffect(int unitId, Pos pos) {
        this.unitId = unitId;
        this.pos = pos;
    }

    public int getUnitId() { return unitId; }
    public Pos getPos() { return pos; }
}
EOF

# --- app/effects/HighlightEffect.java ---
cat > app/effects/HighlightEffect.java <<'EOF'
package app.effects;

import app.structures.basic.Pos;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - HighlightEffect supports modes for move/attack/summon (doc mentions modes).
 * - Sprint 2 only uses SUMMON tiles highlighting.
 */
public final class HighlightEffect implements GameEffect {
    public enum Mode { MOVE, ATTACK, SUMMON }

    private final List<Pos> positions;
    private final Mode mode;

    public HighlightEffect(List<Pos> positions, Mode mode) {
        this.positions = (positions == null) ? List.of() : List.copyOf(positions);
        this.mode = mode;
    }

    public List<Pos> getPositions() { return Collections.unmodifiableList(positions); }
    public Mode getMode() { return mode; }
}
EOF

# --- app/systems/CardSystem.java ---
cat > app/systems/CardSystem.java <<'EOF'
package app.systems;

import app.abilities.Status;
import app.actions.PlayCardAction;
import app.effects.ErrorEffect;
import app.effects.GameEffect;
import app.effects.ManaChangeEffect;
import app.effects.StateSyncEffect;
import app.effects.SummonEffect;
import app.structures.GameState;
import app.structures.PlayerState;
import app.structures.basic.Card;
import app.structures.basic.Pos;
import app.structures.basic.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Systems are the ONLY layer mutating GameState.
 * - CardSystem handles spending mana, removing card, and placing unit on board.
 * - UI is NEVER called here; only effects returned.
 *
 * Edge cases enforced (Sprint 2 requirements):
 * - Card unaffordable -> no-op (ErrorEffect + no state change)
 * - Card not in hand -> no-op
 * - Invalid target tile -> no-op
 * - Insufficient mana -> no-op
 */
public final class CardSystem {

    private CardSystem() {}

    public static List<GameEffect> playCard(GameState state, PlayCardAction action) {
        List<GameEffect> effects = new ArrayList<>();

        if (state == null || action == null) {
            effects.add(new ErrorEffect("Invalid playCard call: null state/action"));
            return effects;
        }

        final int playerId = action.getPlayerId();
        final int handIndex = action.getHandIndex();
        final Pos target = action.getTargetPos();

        // Validate through Rules (no mutation)
        if (!Rules.canPlayCreatureAt(state, playerId, handIndex, target)) {
            effects.add(new ErrorEffect("Illegal creature summon"));
            return effects;
        }

        PlayerState ps = state.getPlayer(playerId);
        Card card = ps.getHand().get(handIndex);
        if (card == null) {
            effects.add(new ErrorEffect("Card not in hand"));
            return effects;
        }

        // Spend mana
        int newMana = ps.getCurrentMana() - card.getManaCost();
        if (newMana < 0) {
            effects.add(new ErrorEffect("Insufficient mana"));
            return effects;
        }
        ps.setCurrentMana(newMana);

        // Remove card from hand
        Card removed = ps.getHand().removeAt(handIndex);
        if (removed == null) {
            effects.add(new ErrorEffect("Card removal failed"));
            return effects;
        }

        // Create and place unit
        int unitId = state.allocateUnitId();
        Unit summoned = new Unit(unitId, playerId, removed.getAttack(), removed.getHealth(), target);

        // Apply summoning sickness (future-safe for Rush keyword):
        // - In Sprint 4, Rush can be implemented by not adding this status for RUSH units
        summoned.addStatus(Status.SUMMONING_SICKNESS);

        state.addUnit(summoned);

        // Clear selection after successful play (selection is UI-oriented state)
        state.setSelectedCardIndex(null);

        // Return effects
        effects.add(new ManaChangeEffect(playerId, ps.getCurrentMana()));
        effects.add(new SummonEffect(unitId, target));
        effects.add(new StateSyncEffect());

        return effects;
    }
}
EOF

# --- app/systems/GameEngine.java ---
cat > app/systems/GameEngine.java <<'EOF'
package app.systems;

import app.actions.GameAction;
import app.actions.PlayCardAction;
import app.effects.GameEffect;
import app.structures.GameState;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - GameEngine is the ONLY entry point for applying actions (doc).
 * - Validation is in Rules; mutation in Systems.
 * - Sprint 2 only routes PlayCardAction to CardSystem.
 */
public final class GameEngine {

    private GameEngine() {}

    public static List<GameEffect> apply(GameState state, GameAction action) {
        if (state == null || action == null) return Collections.emptyList();

        if (action instanceof PlayCardAction) {
            return CardSystem.playCard(state, (PlayCardAction) action);
        }

        // Sprint 2 scope: ignore other actions
        return Collections.emptyList();
    }
}
EOF

# --- app/events/CardClicked.java ---
mkdir -p app/events
cat > app/events/CardClicked.java <<'EOF'
package app.events;

import app.effects.GameEffect;
import app.effects.HighlightEffect;
import app.structures.GameState;
import app.systems.Rules;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Event handler creates NO actions; it may return highlight effects directly
 *   (or in your real repo, it delegates to HumanController).
 * - Story 12 requires:
 *   * Clicking affordable card selects it
 *   * Highlights valid summon tiles for creature cards
 *   * Clicking unaffordable card does nothing
 */
public final class CardClicked {

    private CardClicked() {}

    public static List<GameEffect> onCardClicked(GameState state, int playerId, int handIndex) {
        if (state == null) return Collections.emptyList();

        if (!Rules.canSelectCard(state, playerId, handIndex)) {
            // no-op: clicking unaffordable card does nothing
            return Collections.emptyList();
        }

        // Store selection (could be controller-level in your real repo)
        state.setSelectedCardIndex(handIndex);

        // Highlight valid creature summon tiles (Sprint 2 only)
        List<?> tiles = Rules.getValidSummonTiles(state, playerId);
        @SuppressWarnings("unchecked")
        List<app.structures.basic.Pos> positions = (List<app.structures.basic.Pos>) tiles;

        return List.of(new HighlightEffect(positions, HighlightEffect.Mode.SUMMON));
    }
}
EOF

# --- app/events/TileClicked.java ---
cat > app/events/TileClicked.java <<'EOF'
package app.events;

import app.actions.PlayCardAction;
import app.effects.GameEffect;
import app.structures.GameState;
import app.structures.basic.Pos;
import app.systems.GameEngine;
import app.systems.Rules;

import java.util.Collections;
import java.util.List;

/**
 * ASSUMPTIONS:
 * - Clicking a tile with a selected creature card attempts to play it.
 * - Invalid tile -> rejected safely (no-op or ErrorEffect from CardSystem).
 * - No valid summon tiles -> highlight effect will be empty; this click will be illegal.
 */
public final class TileClicked {

    private TileClicked() {}

    public static List<GameEffect> onTileClicked(GameState state, int playerId, Pos target) {
        if (state == null || target == null) return Collections.emptyList();

        Integer selected = state.getSelectedCardIndex();
        if (selected == null) return Collections.emptyList();

        // Validate target before creating action (keeps controller logic simple; Rules is pure)
        if (!Rules.canPlayCreatureAt(state, playerId, selected, target)) {
            return Collections.emptyList();
        }

        return GameEngine.apply(state, new PlayCardAction(playerId, selected, target));
    }
}
EOF

echo "✅ Sprint 2 patch files created under ./app"
echo "Next: add your build tool (Maven/Gradle) and compile, or map into your real Sprint 1 repo structure."
