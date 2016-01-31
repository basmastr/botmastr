package botmastr;

import bwapi.Unit;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class AUnitObjective implements Comparable<AUnitObjective> {
    /**
     * Priority of the request. Reqest with higher priority will get accepted sooner in case of multiple requests.
     */
    protected EPriority priority;

    /**
     *
     * @param unit
     */
    public abstract void execute(UnitData unit);

    public AUnitObjective() {
        this.priority = EPriority.LOW;
    }

    public AUnitObjective(EPriority priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(AUnitObjective o) {
        return this.priority.compareTo(o.getPriority());
    }

    public EPriority getPriority() {
        return this.priority;
    }
}
