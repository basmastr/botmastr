package botmastr.production.building;

/**
 * Possible states of BuildingQueueItem.
 * @see BuildingQueueItem
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public enum EBuildingQueueItemStates {
    /**
     * Default state before queueing of the item.
     */
    UNQUEUED,
    /**
     * Item has been inserted into the bulding queue.
     */
    QUEUED,
    AWAITING_RESOURCES_ALLOCATION,
    AWAITING_WORKER_ALLOCATION,
    WORKER_EN_ROUTE,
    BUILDING;
}
