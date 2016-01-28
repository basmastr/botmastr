package botmastr;

import java.util.PriorityQueue;

import bwapi.Unit;

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
    protected PriorityQueue<IUnitObjective> plan;

    public UnitData(Unit unit) {
        this.unit = unit;
        this.plan = new PriorityQueue<IUnitObjective>();
    }

    public Unit getUnit() {
        return this.unit;
    }

    public PriorityQueue<IUnitObjective> getPlan() {
        return this.plan;
    }

    /**
     * Adds an objective to this units plan.
     * @param newObjective New objective.
     */
    public void addObjective(IUnitObjective newObjective) {
        final IUnitObjective oldObjective = this.plan.peek();
        this.plan.add(newObjective);
        if (!oldObjective.equals(this.plan.peek())) {
            newObjective.execute(this);
        }
    }
}
