import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Coordinates represents a location in terms of x-y coordinates
 */
public class Coordinates {
    /** x, y coordinates of the location */
    private double x, y;

    // Abstract Function:
    // Each Coordinates, c, represents a location in terms of (x, y) coordinates
    // Rep Invariant:
    // None

    /**
     * Constructs a new Coordinates object representing the given points
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @spec.effects Constructs a new Coordinates object representing the given points
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter method of this's x coordinate
     * @return this's x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x coordinate of this to the given x
     * @param x the new x coordinate to be set to
     * @spec.modifies this's x coordinate
     * @spec.effects this's x coordinate is changed to the given "x"
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Getter method of this's y coordinate
     * @return this's y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y coordinate of this to the given y
     * @param y the new y coordinate to be set to
     * @spec.modifies this's y coordinate
     * @spec.effects this's y coordinate is changed to the given "y"
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Standard hashCode function
     *
     * @return an int that all objects equal to this will also return
     */
    @Override
    public int hashCode() {
        return (int) x + (int) (y * 11);
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'obj' is an instance of a Coordinates and 'this' and 'obj'
     *         have the same values
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Coordinates) {
            Coordinates other = (Coordinates) obj;
            return this.x == other.getX() && this.y == other.getY();
        }
        return false;
    }
}
