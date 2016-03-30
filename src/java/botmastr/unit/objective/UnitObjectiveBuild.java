package botmastr.unit.objective;

import botmastr.common.Common;
import botmastr.production.building.BuildingQueueItem;
import botmastr.production.building.EBuildingQueueItemStates;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import bwapi.Color;
import bwapi.Position;
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
    protected UnitType building;

    /**
     * Where to build.
     */
    protected TilePosition position;

    protected BuildingQueueItem buildingQueueItem;

    protected int failsafeCounter;

    protected static final int FAIL_SAFE_COUNTER_MAX = 100;

    protected Position builderLastPosition;

    /**
     *
     * @param unit
     * @param building
     * @param position
     */
    public UnitObjectiveBuild(UnitData unit, BuildingQueueItem buildingQueueItem, TilePosition position) {
        super(unit);
        this.builderLastPosition = unit.getUnit().getPosition();
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
        this.builderLastPosition = unit.getUnit().getPosition();
        this.building = buildingQueueItem.getBuilding();
        this.buildingQueueItem = buildingQueueItem;
        this.position = position;
    }

    @Override
    public void execute() {

    }

    public void tic() {
        if (Common.getInstance().getGame().canMake(this.building)) {
            if (this.unit.getUnit().canBuild(this.building, this.position)) {
                this.unit.getUnit().build(this.building, this.position);
            }
            else {
                if (this.unit.getUnit().getPosition().equals(this.builderLastPosition)) {
                    this.failsafeCounter++;
                }
                else {
                    this.failsafeCounter = 0;
                    this.builderLastPosition = this.unit.getUnit().getPosition();
                }
                if (this.failsafeCounter == UnitObjectiveBuild.FAIL_SAFE_COUNTER_MAX) {
                    fail();
                }
            }
        }

        if (Common.getInstance().debug()) {
            debug();
        }
    }

    private void debug() {
        //draw the square where the building should be
        Common.getInstance().getGame().drawBoxMap(this.position.toPosition(), new Position(this.position.toPosition().getX()+this.building.width()+32,this.position.toPosition().getY()+this.building.height()+32), Color.Orange);
    }

    @Override
    public void finish() {
        super.finish();
        this.buildingQueueItem.setState(EBuildingQueueItemStates.BUILDING);
    }

    public void fail() {
        this.buildingQueueItem.setState(EBuildingQueueItemStates.AWAITING_WORKER_ALLOCATION);
        super.finish();
    }


    public TilePosition getPosition() {
        return this.position;
    }

    public UnitType getBuilding() {
        return this.building;
    }

    @Override
    public void unitDestroyed() {
        fail();
    }

    @Override
    public String getName() {
        return "BuildObjective_" + this.getBuilding().toString() + "_" + this.failsafeCounter;
    }
}
