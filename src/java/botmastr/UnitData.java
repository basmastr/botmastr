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
    protected PriorityQueue<AUnitObjective> plan = new PriorityQueue<>();

    public UnitData(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public PriorityQueue<AUnitObjective> getPlan() {
        return this.plan;
    }

    /**
     * Adds an objective to this units plan.
     * @param newObjective New objective.
     */
    public void addObjective(AUnitObjective newObjective) {
        final AUnitObjective oldObjective = this.plan.peek();
        this.plan.add(newObjective);
        if (oldObjective == null || !oldObjective.equals(this.plan.peek())) {
            //top priority objective has changed, execute it instead of continueing in the last one
            newObjective.execute(this);
        }
    }
}
