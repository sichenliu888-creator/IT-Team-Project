package structures;

import abilities.Status;

import java.util.EnumSet;
import java.util.Set;

/**
 * Runtime unit state: hp, attack, owner, position, flags, keywords and statuses.
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
