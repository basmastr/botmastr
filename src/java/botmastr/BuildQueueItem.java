package java.botmastr;

import bwapi.UnitType;

/**
 * Single item in build queue.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class BuildQueueItem implements Comparable<BuildQueueItem> {
    /**
     * Priority of this item in the quere (higher means it will be built sooner).
     */
    protected EPriority priority;
    /**
     * What bulding should be built.
     */
    protected UnitType building;
    /**
     * Preferred base where to build this building. NULL means it doesn't matter.
     */
    protected Base base;

    /**
     * Constructor for item without explicitly set base to be built in.
     * @param priority priority of the item in the build queue
     * @param building what building to build
     */
    public BuildQueueItem(EPriority priority, UnitType building) {
        this.priority = priority;
        this.building = building;
        this.base = null;
    }

    /**
     * Constructor for item with explicitly set base in which it should be built.
     * @param priority priority of the item in the build queue
     * @param building what building to build
     * @param base where to build
     */
    public BuildQueueItem(EPriority priority, UnitType building, Base base) {
        this.priority = priority;
        this.building = building;
        this.base = base;
    }

    @Override
    public int compareTo(BuildQueueItem o) {
        return this.priority.compareTo(o.getPriority());
    }

    public EPriority getPriority() {
        return this.priority;
    }

    public UnitType getBuilding() {
        return this.building;
    }

    public Base getBase() {
        return this.base;
    }
}
