package botmastr;

import java.util.Collection;
import java.util.PriorityQueue;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class PriorityQueueInsertOrdered<E extends APQInsertable> extends PriorityQueue<E> {
    protected Long insertCount = 0L;

//    @Override
//        public boolean add(E o) {
//            o.setInsertOrder(this.insertCount);
//            final boolean ret = super.add(o);
//            this.insertCount++;
//            return ret;
//    }
    @Override
        public boolean offer(E o) {
            // check neccessary cos offer is used by constructor from collection aswell
            if (!o.isInserted()) {
                o.setInsertOrder(this.insertCount);
            }
            final boolean ret = super.offer(o);
            this.insertCount++;
            return ret;
    }
//    @Override
//        public boolean addAll(Collection<? extends E> o) {
//            final boolean ret = super.addAll(o);
//            this.insertCount += o.size();
//            return ret;
//    }
}
