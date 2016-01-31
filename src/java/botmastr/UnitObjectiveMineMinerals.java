package botmastr;

import bwapi.Unit;

/**
 * Implements single task for a unit.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveMineMinerals extends AUnitObjective {
    /**
     * Unit representing the mineral patch to be mined.
     */
    protected Unit mineral;

    public UnitObjectiveMineMinerals(Unit mineral) {
        super();
        this.mineral = mineral;
    }

    /**
     *
     * @param mineral Unit representing the mineral patch to be mined.
     */
    public UnitObjectiveMineMinerals(Unit mineral, EPriority priority) {
        super(priority);

        this.mineral = mineral;
    }

    /**
     * Execute the behaviour needed for completion of this objective.
     * @param unit
     */
    @Override
    public void execute(UnitData unit) {
        System.out.println("execute unit.getUnit().getType().toString() = " + unit.getUnit().getType().toString());
        unit.getUnit().gather(this.mineral);
    }
}
