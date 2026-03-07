package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile. 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	int id;
	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;
	
	public Unit() {}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(0,0,0,0);
		this.correction = correction;
		this.animations = animations;
	}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}
	
	
	
	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UnitAnimationType getAnimation() {
		return animation;
	}
	public void setAnimation(UnitAnimationType animation) {
		this.animation = animation;
	}

	public ImageCorrection getCorrection() {
		return correction;
	}

	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public UnitAnimationSet getAnimations() {
		return animations;
	}

	public void setAnimations(UnitAnimationSet animations) {
		this.animations = animations;
	}
	
	/**
	 * This command sets the position of the Unit to a specified
	 * tile.
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
	}
	
	
}
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

