package structures;

import structures.basic.Unit;

public class GameUnit {

    private Unit unit;            // template Unit object
    private int owner;            // 1 is for human and 2 is for AI
    private int attack;
    private int health;
    private int maxHealth;
    private boolean hasMoved;
    private boolean hasAttacked;
    private boolean isAvatar;
    private int tileX;
    private int tileY;
    private String cardName;

    // constructor
    public GameUnit(Unit unit, int owner, int attack, int health, boolean isAvatar) {
        this.unit = unit;
        this.owner = owner;
        this.attack = attack;
        this.health = health;
        this.maxHealth = health;
        this.hasMoved = false;
        this.hasAttacked = false;
        this.isAvatar = isAvatar;
        this.tileX = 0;
        this.tileY = 0;
    }

    // setters and getters

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public int getOwner() { return owner; }
    public void setOwner(int owner) { this.owner = owner; }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }

    public boolean hasAttacked() { return hasAttacked; }
    public void setHasAttacked(boolean hasAttacked) { this.hasAttacked = hasAttacked; }

    public boolean isAvatar() { return isAvatar; }
    public void setIsAvatar(boolean isAvatar) { this.isAvatar = isAvatar; }

    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
    public void setPosition(int x, int y) {
        this.tileX = x;
        this.tileY = y;
    }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }


    // game logic methods

    public void takeDamage(int dmg) {
        this.health -= dmg;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    public void resetTurnFlags() {
        this.hasMoved = false;
        this.hasAttacked = false;
    }
    
}
