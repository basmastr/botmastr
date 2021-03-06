package botmastr.production;

import botmastr.common.Cost;
import botmastr.common.EPriority;
import botmastr.common.PriorityQueueInsertCountedItem;

/**
 * Base class for queue items that require resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class AProductionQueueItem extends PriorityQueueInsertCountedItem implements Comparable<AProductionQueueItem> {
    /**
     * Priority of this item in the quere (higher means it will be allocated resources sooner).
     */
    protected EPriority priority;


    public AProductionQueueItem(AProductionQueueItem other) {
        super(other);
        this.priority = other.priority;
    }

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
        final int ret = this.priority.compareTo(o.getPriority());

        if (ret == 0) {
            return super.compareTo(o);
        }

        return ret;
    }

    public EPriority getPriority() {
        return this.priority;
    }
}
