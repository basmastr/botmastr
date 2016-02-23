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
     * True if the objective is finished and is waiting to be deleted, False if it's still active.
     */
    protected Boolean finished = false;

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
     *
     */
    public abstract void execute();

    /**
     * Periodically ran by UnitManager to execute necessary checks and actions.
     */
    public abstract void tic();

    /**
     * Starts the objective by adding it to the list of currently active objectives.
     */
    public void start() {
        UnitManager.getInstance().addActiveObjective(this);
    }

    /**
     * Finalizes the objective by removing it from the list of currently active objectives.
     */
    public void finish() {
        this.finished = true;
        UnitManager.getInstance().addFinishedObjective(this);
    }

    /**
     * Does the final cleanup after the objective before it is deleted.
     * Do not call this method in tic or execute methods!
     */
    public void delete()  {
        if (this.finished) {
            // TODO: 19.2.2016 solve exception throwing  ObjectiveNotFinishedProperlyException
            this.unit.objectiveFinished(this);
        }
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
     * Checks if the objective is already finished.
     * @return True if objective is finished, False otherwise.
     */
    public Boolean isFinished() {
        return this.finished;
    }

    /**
     * Checks if the objective is still active.
     * @return False if objective is finished, True otherwise.
     */
    public Boolean isActive() {
        return !this.finished;
    }

    /**
     * Forces subclasses to implement name for debugging purposes.
     * @return Name of the objective.
     */
    public abstract String getName();

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
