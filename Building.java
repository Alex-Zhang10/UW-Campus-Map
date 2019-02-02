package hw8;

import org.checkerframework.checker.nullness.qual.*;

/**
 * Building represents the information of a building on campus, including name and location
 */
public final class Building {

    /** the abbrev. name */
    private final String shortName;

    /** the full name */
    private final String longName;

    /** the x-y coordinate of the building */
    private final Coordinates location;

    // Abstract Function:
    // Each building b stores b's abbrev. name, full name, and x-y coordinate location
    // Rep Invariant:
    // shortName != null && longName != null && location != null

    /**
     * Construct a new Building object
     * @spec.requires {@code shortName != null && longName != null && location != null}
     * @param shortName the abbrev. name
     * @param longName the full name
     * @param location the x-y coordinate of the building
     * @spec.effects Construct a new Building object with the given shortName, longName, location
     */
    public Building(String shortName, String longName, Coordinates location) {
        this.shortName = shortName;
        this.longName = longName;
        this.location = location;
        checkRep();
    }

    /**
     * Getter method for the abbrev. name of this Building
     * @return a String representing the abbrev. name of this Building
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Getter method for the full name of this Building
     * @return a String representing the full name of this Building
     */
    public String getLongName() {
        return this.longName;
    }

    /**
     * Getter method for the location of this Building
     * @return a Coordinates object representing the location of this Building
     */
    public Coordinates getLocation() {
        return this.location;
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep() {
        assert (this.longName != null);
        assert (this.shortName != null);
        assert (this.location != null);
    }

    /**
     * Checks if this Building's name is the same as the given name
     * @param name the name this's name is compared to
     * @return true if either the abbrev. name or the full name of this Building
     * is the same as the given name
     */
    public boolean equalsName(String name) {
        return this.longName.equals(name) || this.shortName.equals(name);
    }

    /**
     * Standard hashCode function
     *
     * @return an int that all objects equal to this will also return
     */
    @Override
    public int hashCode() {
        checkRep();
        return this.shortName.hashCode() + 11 * this.longName.hashCode() +
                location.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'obj' is an instance of a Building and 'this' and 'obj'
     *         have the same values
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Building) {
            Building other = (Building) obj;
            return this.shortName.equals(other.getShortName()) &&
                    this.longName.equals(other.getLongName()) &&
                    this.location.equals(other.getLocation());
        }
        return false;
    }
}
