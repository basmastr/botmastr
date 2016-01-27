package botmastr;

import bwapi.Unit;

import java.util.PriorityQueue;

/**
 * Represents a single BWAPI unit but adds aditional info.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitData {
    /**
     * BWAPI unit tied to this object.
     */
    protected Unit unit;

    /**
     * Plan containing current objectives of this unit.
     */
    protected PriorityQueue<UnitObjective> plan;

    public UnitData(Unit unit) {
        this.unit = unit;
        this.plan = new PriorityQueue<UnitObjective>();
    }

    public Unit getUnit() {
        return this.unit;
    }

    public PriorityQueue<UnitObjective> getPlan() {
        return this.plan;
    }
}
