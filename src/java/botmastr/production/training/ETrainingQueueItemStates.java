package botmastr.production.training;

/**
 * Possible states of TrainingQueueItem.
 * @see TrainingQueueItem
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public enum ETrainingQueueItemStates {

    /**
     * Item will not be produced and it will be deleted as soon as possible.
     */
    CANCELED,

    /**
     * Default state before queueing of the item.
     */
    UNQUEUED,

    /**
     * Item has been inserted into the training manager queue.
     */
    QUEUED,

    /**
     * Item waiting for a viable producer to be found.
     */
    AWAITING_PRODUCER_ALLOCATION,

    /**
     * Item has been allocated a producer.
     */
    HAS_PRODUCER,

    /**
     * Request for resources has been sent and is awaiting approval.
     */
    AWAITING_RESOURCES_ALLOCATION,

    /**
     * Producer and resources have been allocated, waiting for the training to begin.
     */
    AWAITING_TRAINING,

    /**
     * Item has began training by the producer.
     */
    TRAINING,

    /**
     * The represented unit should now exist and be completed.
     */
    COMPLETED {
        @Override
        public ETrainingQueueItemStates next() {
            return null;
        }
    };

    /**
     * Returns the next state.
     * @return next state or null for the last state
     */
    public ETrainingQueueItemStates next() {
        return values()[this.ordinal() + 1];
    }
}
