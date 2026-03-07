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
