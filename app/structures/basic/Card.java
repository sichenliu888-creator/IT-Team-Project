<<<<<<< HEAD
package structures.basic;


/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Card {
	
	int id;
	
	String cardname;
	int manacost;
	
	MiniCard miniCard;
	BigCard bigCard;
	
	boolean isCreature;
	String unitConfig;
	
	public Card() {};
	
	public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard, boolean isCreature, String unitConfig) {
		super();
		this.id = id;
		this.cardname = cardname;
		this.manacost = manacost;
		this.miniCard = miniCard;
		this.bigCard = bigCard;
		this.isCreature = isCreature;
		this.unitConfig = unitConfig;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public int getManacost() {
		return manacost;
	}
	public void setManacost(int manacost) {
		this.manacost = manacost;
	}
	public MiniCard getMiniCard() {
		return miniCard;
	}
	public void setMiniCard(MiniCard miniCard) {
		this.miniCard = miniCard;
	}
	public BigCard getBigCard() {
		return bigCard;
	}
	public void setBigCard(BigCard bigCard) {
		this.bigCard = bigCard;
	}
	public boolean getIsCreature() {
		return isCreature;
	}
	public void setIsCreature(boolean isCreature) {
		this.isCreature = isCreature;
	}
	public void setCreature(boolean isCreature) {
		this.isCreature = isCreature;
	}
	public boolean isCreature() {
		return isCreature;
	}
	public String getUnitConfig() {
		return unitConfig;
	}
	public void setUnitConfig(String unitConfig) {
		this.unitConfig = unitConfig;
	}

	
}
=======
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
>>>>>>> 4bd31c6 (Sprint2: add core state model + summon rules)
