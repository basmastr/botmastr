package botmastr;

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

    /**
     *
     * @param unit
     * @param building
     * @param position
     */
    public UnitObjectiveBuild(UnitData unit, UnitType building, TilePosition position) {
        super(unit);
        this.building = building;
        this.position = position;
    }

    /**
     *
     * @param unit
     * @param building
     * @param position
     * @param priority
     */
    public UnitObjectiveBuild(UnitData unit, UnitType building, TilePosition position, EPriority priority) {
        super(priority, unit);
        this.building = building;
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
        this.unit.getUnit().build(this.position, this.building);
    }

    public TilePosition getPosition() {
        return this.position;
    }

    public UnitType getBuilding() {
        return this.building;
    }

    @Override
    public String getName() {
        return "BuildObjective";
    }
}
