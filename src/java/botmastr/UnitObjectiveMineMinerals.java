package botmastr;

import bwapi.Unit;

/**
 * Implements single task for a unit.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveMineMinerals implements IUnitObjective {
    private Unit mineral;

    public UnitObjectiveMineMinerals(Unit mineral) {
        this.mineral = mineral;
    }

    @Override
    public void execute(UnitData unit) {
        unit.getUnit().gather(this.mineral);
    }
}
