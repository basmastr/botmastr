package botmastr.common;

/**
 * Extending this makes a class usable as item in PriorityQueueInsertCounted.
 * Any item that should be usable with PriorityQueueInsertCounted needs to extend this class.
 * @see PriorityQueueInsertCounted
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class PriorityQueueInsertCountedItem {
    /**
     * Order in which this item was inserted into the queue.
     * @see PriorityQueueInsertCounted
     */
    protected Long insertOrder = 0L;

    public PriorityQueueInsertCountedItem() {
    }

    public PriorityQueueInsertCountedItem(PriorityQueueInsertCountedItem other) {
        this.insertOrder = other.insertOrder;
    }


    public boolean isInserted() {
        return this.insertOrder != 0L;
    }

    public Long getInsertOrder() {
        return this.insertOrder;
    }

    public void setInsertOrder(Long insertOrder) {
        this.insertOrder = insertOrder;
    }

    public int compareTo(PriorityQueueInsertCountedItem o) {
        return this.insertOrder.compareTo(o.getInsertOrder());
    }
}
