package botmastr;

import bwapi.Unit;

/**
 * Objective for a worker to mine a mineral patch.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveMineMinerals extends AUnitObjective {
    /**
     * Unit representing the mineral patch to be mined.
     */
    protected Unit mineral;

    public UnitObjectiveMineMinerals(UnitData unit, Unit mineral) {
        super(unit);
        this.mineral = mineral;
    }

    /**
     *
     * @param unit
     * @param mineral Unit representing the mineral patch to be mined.
     */
    public UnitObjectiveMineMinerals(UnitData unit, Unit mineral, EPriority priority) {
        super(priority, unit);
        this.mineral = mineral;
    }

    /**
     * Execute the behaviour needed for completion of this objective.
     */
    @Override
    public void execute() {
        start();
        unit.getUnit().gather(this.mineral);
    }


    public void tic() {
    }

    @Override
    public String getName() {
        return "MineMineralsObjective";
    }
}
