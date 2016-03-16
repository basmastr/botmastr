package botmastr.unit.objective;

import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import bwapi.Color;
import bwapi.Unit;

/**
 * Objective for a worker to mine gas from a refinery.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveMineGas extends AUnitObjective {
    /**
     * Unit representing the refinery to be mined from.
     */
    protected Unit refinery;

    public UnitObjectiveMineGas(UnitData unit, Unit refinery) {
        super(unit);
        this.refinery = refinery;
    }

    /**
     *
     * @param unit
     * @param refinery Unit representing the refinery patch to be mined.
     */
    public UnitObjectiveMineGas(UnitData unit, Unit refinery, EPriority priority) {
        super(priority, unit);
        this.refinery = refinery;
    }

    /**
     * Execute the behaviour needed for completion of this objective.
     */
    @Override
    public void execute() {
        start();
        unit.getUnit().gather(this.refinery);
    }

    public Unit getRefinery() {
        return this.refinery;
    }

    /**
     * Changes the refinery patch to mine.
     * @param refinery new refinery to mine
     */
    public void setRefinery(Unit refinery) {
        this.refinery = refinery;
    }

    public void tic() {
//        final Unit unit = this.unit.getUnit();
//        if (!unit.getTarget().equals(this.refinery)) {
//            unit.gather(this.refinery);
//        }
        debug();
    }

    @Override
    public void unitDestroyed() {
        finish();
    }

    @Override
    public String getName() {
        return "MineGasObjective";
    }


    public void debug() {
        Common.getInstance().getGame().drawLineMap(this.unit.getUnit().getPosition(), this.refinery.getPosition(), Color.White);
    }
}
