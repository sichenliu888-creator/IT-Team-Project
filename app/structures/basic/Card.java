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
