package botmastr;

import java.util.Collection;
import java.util.PriorityQueue;

/**
 * Priority queue extended to keep track of how many items were added here in total.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 * @param <E> Item insertable into this collection.
 */
public class PriorityQueueInsertCounted<E extends PriorityQueueInsertCountedItem> extends PriorityQueue<E> {

    /**
     * Number of items that were inserted into this collection (including those already removed).
     * If item with insertOrder higher than insertCount is offered, insertCount is increased accordingly.
     */
    protected Long insertCount = 0L;

    public PriorityQueueInsertCounted() {
    }

    public PriorityQueueInsertCounted(Collection<E> c) {
        super(c);

        if (c instanceof PriorityQueueInsertCounted) {
            this.insertCount =  ((PriorityQueueInsertCounted) c).insertCount;
        }
    }

    @Override
    public boolean offer(E o) {
        if (!o.isInserted()) {
            o.setInsertOrder(this.insertCount);
            this.insertCount++;
        }
        else {
            if (o.getInsertOrder() > this.insertCount) {
                this.insertCount = o.getInsertOrder() + 1;
            }
        }

        return super.offer(o);
    }
}
