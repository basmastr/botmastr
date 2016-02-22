package botmastr;

import bwapi.UnitType;

/**
 * Single item in build queue.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class BuildingQueueItem extends AProductionQueueItem {

    /**
     * What bulding should be built.
     */
    protected UnitType building;

    /**
     * Preferred base where to build this building. NULL means it doesn't matter.
     */
    protected MyBase base;

    /**
     * Current state of this item.
     */
    protected EBuildingQueueItemStates state;

    /**
     * Constructor for item without explicitly set base to be built in.
     * @param priority priority of the item in the build queue
     * @param building what building to build
     */
    public BuildingQueueItem(EPriority priority, UnitType building) {
        super(priority);
        this.building = building;
        this.base = null;
        this.state = EBuildingQueueItemStates.UNQUEUED;
    }

    /**
     * Constructor for item with explicitly set base in which it should be built.
     * @param priority priority of the item in the build queue
     * @param building what building to build
     * @param base where to build
     */
    public BuildingQueueItem(EPriority priority, UnitType building, MyBase base) {
        super(priority);
        this.building = building;
        this.base = base;
        this.state = EBuildingQueueItemStates.UNQUEUED;
    }

    @Override
    public Cost getCost() {
        return new Cost(this.building.mineralPrice(), this.building.gasPrice());
    }

    public void setState(EBuildingQueueItemStates state) {
        this.state = state;
    }

    public UnitType getBuilding() {
        return this.building;
    }

    public MyBase getBase() {
        return this.base;
    }

    public EBuildingQueueItemStates getState() {
        return this.state;
    }
}
