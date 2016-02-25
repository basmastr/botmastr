package botmastr.production.training;

import java.util.stream.Collectors;

import botmastr.common.AManager;
import botmastr.common.Common;
import botmastr.common.PriorityQueueInsertCounted;
import botmastr.production.resources.IResourcesRequestor;
import botmastr.production.resources.ResourceManager;
import botmastr.production.resources.ResourcesRequest;
import bwapi.Unit;

/**
 * Takes care of keeping a list of units to train and actually training them.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class TrainingManager extends AManager implements IResourcesRequestor {

    /**
     * Singleton instance.
     */
    private static final TrainingManager INSTANCE = new TrainingManager();

    /**
     * Queue of buildings to build next.
     */
    protected PriorityQueueInsertCounted<TrainingQueueItem> queue = new PriorityQueueInsertCounted<>();

    /**
     * Private because this is a singleton.
     */
    private TrainingManager() {
    }

    public static TrainingManager getInstance() {
        return INSTANCE;
    }

    /**
     * Request for recources has been accepted by the resource manager
     * @param request Request that has been accepted.
     */
    @Override
    public void requestAccepted(ResourcesRequest request) {
        final TrainingQueueItem item = (TrainingQueueItem) request.getReason();
        item.advanceState();
   }

    @Override
    public void requestDenied(ResourcesRequest request) {
        ((TrainingQueueItem) request.getReason()).reset();
    }

    @Override
    public void tic() {
        handleQueuedItems();
        handleAwaitingProducerItems();
        handleHasProducerItems();
        handleAwaitingTrainingItems();
    }


    protected void handleQueuedItems() {
        final PriorityQueueInsertCounted<TrainingQueueItem> queued = getQueueItemsByState(ETrainingQueueItemStates.QUEUED);
        if (!queued.isEmpty()) {
            final TrainingQueueItem item = queued.peek();
            item.advanceState();
        }
    }

    protected void handleAwaitingProducerItems() {
        final PriorityQueueInsertCounted<TrainingQueueItem> items = getQueueItemsByState(ETrainingQueueItemStates.AWAITING_PRODUCER_ALLOCATION);

        for (TrainingQueueItem item :
                items) {

            final Unit producer = getProducer(item);

            if (producer != null) {
                item.setProducer(producer);
                item.advanceState();
            }
        }
    }

    protected void handleHasProducerItems() {
        final PriorityQueueInsertCounted<TrainingQueueItem> items = getQueueItemsByState(ETrainingQueueItemStates.HAS_PRODUCER);

        for (TrainingQueueItem item :
                items) {

            final ResourcesRequest request = new ResourcesRequest(item.getTrainee().mineralPrice(), item.getTrainee().gasPrice(), item, this);
            request.send();
            item.advanceState();
        }
    }

    protected void handleAwaitingTrainingItems() {
        final PriorityQueueInsertCounted<TrainingQueueItem> items = getQueueItemsByState(ETrainingQueueItemStates.AWAITING_TRAINING);

        for (TrainingQueueItem item :
                items) {

            if (item.train()) {
                item.advanceState();
                ResourceManager.getInstance().requestMaterialized(item.getCost());
                // TODO: 25.2.2016 remove
                this.queue.remove(item);
            }
        }
        // TODO: 25.2.2016 make producer UnitData and TrainingItem an observer of producer and make producer notify the item when it finished training
    }



    /**
     * Finds a suitable producer for training item.
     * @return Chosen producer or null if no producer was found.
     */
    protected Unit getProducer(TrainingQueueItem item) {
        // TODO: 25.2.2016 get closest to item.whereNeeded
        return Common.getInstance().getPlayer().getUnits().stream().filter(u -> u.canTrain(item.getTrainee())).findFirst().orElse(null);
    }

    /**
     * Filters training items in queue by their state.
     * @param state state to filter by
     * @return Collection of training items in the supplied state.
     */
    private PriorityQueueInsertCounted<TrainingQueueItem> getQueueItemsByState(ETrainingQueueItemStates state) {
        return this.queue.stream().filter(i -> i.getState().equals(state)).collect(Collectors.toCollection(PriorityQueueInsertCounted::new));
    }


    /**
     * Insert an item into the training queue.
     * @param item item to be trained
     */
    public void addQueueItem(TrainingQueueItem item) {
        this.queue.add(item);
        item.advanceState();
    }
}
