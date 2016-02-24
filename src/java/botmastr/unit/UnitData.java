package botmastr.unit;

import java.util.PriorityQueue;

import botmastr.unit.objective.AUnitObjective;
import bwapi.Position;
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
        final AUnitObjective newTopObj = this.plan.peek();

        if (oldObjective == null || !oldObjective.equals(this.plan.peek())) {
            newObjective.execute();
        }
    }


    /**
     * Notifies unit that it's objective has finished.
     */
    public void objectiveFinished(AUnitObjective objective) {
        this.plan.remove(objective);

        if (this.plan.peek() != null) {
            this.plan.peek().execute();
        }
    }

    /**
     * Print debugging info for the unit.
     */
    public void debug() {
        int y = this.unit.getY();
        for (AUnitObjective o: this.plan) {
            o.debug(new Position(this.unit.getX(), y));
            y += 10;
        }
    }

    /**
     * Checks if this unit is doing something.
     * @return True if unit has no objectives, false otherwise.
     */
    public boolean idle() {
        return this.plan.size() == 0;
    }
}
