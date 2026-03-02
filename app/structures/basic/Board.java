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
