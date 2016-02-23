package botmastr.unit.objective;

import botmastr.production.building.BuildingQueueItem;
import botmastr.production.building.EBuildingQueueItemStates;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import bwapi.TilePosition;
import bwapi.UnitType;

/**
 * Objective for a worker to build a building.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveBuild extends AUnitObjective {
    /**
     * Building to be built.
     */
    private UnitType building;

    /**
     * Where to build.
     */
    private TilePosition position;

    protected BuildingQueueItem buildingQueueItem;
    /**
     *
     * @param unit
     * @param building
     * @param position
     */
    public UnitObjectiveBuild(UnitData unit, BuildingQueueItem buildingQueueItem, TilePosition position) {
        super(unit);
        this.building = buildingQueueItem.getBuilding();
        this.buildingQueueItem = buildingQueueItem;
        this.position = position;
    }

    /**
     *
     * @param unit
     * @param building
     * @param position
     * @param priority
     */
    public UnitObjectiveBuild(UnitData unit, BuildingQueueItem buildingQueueItem, TilePosition position, EPriority priority) {
        super(priority, unit);
        this.building = buildingQueueItem.getBuilding();
        this.buildingQueueItem = buildingQueueItem;
        this.position = position;
    }

    /**
     * Execute the behaviour needed for completion of this objective.
     */
    @Override
    public void execute() {
        start();
    }


    public void tic() {
        this.unit.getUnit().build(this.building, this.position);
    }

    @Override
    public void finish() {
        super.finish();
        this.buildingQueueItem.setState(EBuildingQueueItemStates.BUILDING);
    }

    public TilePosition getPosition() {
        return this.position;
    }

    public UnitType getBuilding() {
        return this.building;
    }

    @Override
    public String getName() {
        return "BuildObjective_" + this.getBuilding().toString();
    }
}
