package botmastr.unit.objective;

import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import botmastr.unit.UnitManager;
import bwapi.Position;

/**
 * Base class for unit objectives.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class AUnitObjective implements Comparable<AUnitObjective> {
    /**
     * Priority of the request. Reqest with higher priority will get accepted sooner in case of multiple requests.
     */
    protected EPriority priority;

    /**
     * Unit which is assigned this objective.
     */
    protected UnitData unit;

    /**
     *
     * @param unit
     */
    public AUnitObjective(UnitData unit) {
        this.priority = EPriority.LOW;
        this.unit = unit;
    }

    public AUnitObjective(EPriority priority, UnitData unit) {
        this.priority = priority;
        this.unit = unit;
    }

    /**
     * Behaviour executed once before the first execution of {@code tic()}.
     */
    public abstract void execute();

    /**
     * Behaviour ran each frame to execute necessary checks and actions of this objective.
     */
    public abstract void tic();

    /**
     * Finalizes the objective by removing it from the list of currently active objectives.
     */
    public void finish() {
        this.unit.objectiveFinished(this);
    }

    @Override
    public int compareTo(AUnitObjective o) {
        return this.priority.compareTo(o.getPriority());
    }

    public UnitData getUnit() {
        return this.unit;
    }

    public EPriority getPriority() {
        return this.priority;
    }

    /**
     * Forces subclasses to implement name for debugging purposes.
     * @return Name of the objective.
     */
    public abstract String getName();

    /**
     * Reacts to the {@code Unit} executing this objective being destroyed.
     */
    public abstract void unitDestroyed();

    /**
     * Print debugging information for the objective.
     * @param position Where to print.
     */
    public void debug(Position position) {
        Common.getInstance().getGame().drawTextMap(position.getX(), position.getY(), getName());
    }

    protected class ObjectiveNotFinishedProperlyException extends Exception {

    }
}
