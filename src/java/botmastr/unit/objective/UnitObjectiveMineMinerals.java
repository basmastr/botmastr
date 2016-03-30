package botmastr.unit.objective;

import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import bwapi.Color;
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
        unit.getUnit().gather(this.mineral);
    }

    public Unit getMineral() {
        return this.mineral;
    }

    /**
     * Changes the mineral patch to mine.
     * @param mineral new mineral to mine
     */
    public void setMineral(Unit mineral) {
        this.mineral = mineral;
    }

    public void tic() {
        if (Common.getInstance().debug()) {
            debug();
        }
    }

    @Override
    public void unitDestroyed() {
        finish();
    }

    @Override
    public String getName() {
        return "MineMineralsObjective";
    }


    private void debug() {
        Common.getInstance().getGame().drawLineMap(this.unit.getUnit().getPosition(), this.mineral.getPosition(), Color.White);
    }
}
