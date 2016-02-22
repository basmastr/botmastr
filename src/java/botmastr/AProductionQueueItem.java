package botmastr;

/**
 * Base class for queue items that require resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class AProductionQueueItem implements Comparable<AProductionQueueItem> {
    /**
     * Priority of this item in the quere (higher means it will be allocated resources sooner).
     */
    protected EPriority priority;

    /**
     *
     * @param priority
     */
    public AProductionQueueItem(EPriority priority) {
        this.priority = priority;
    }

    public abstract Cost getCost();

    @Override
    public int compareTo(AProductionQueueItem o) {
        return this.priority.compareTo(o.getPriority());
    }

    public EPriority getPriority() {
        return this.priority;
    }
}
