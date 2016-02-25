package botmastr.production.training;

import botmastr.common.Cost;
import botmastr.common.EPriority;
import botmastr.production.AProductionQueueItem;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Single item in training queue.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class TrainingQueueItem extends AProductionQueueItem {

    /**
     * What bulding should be built.
     */
    protected UnitType trainee;

    /**
     * Where is the trainee needed, might affect which producer is picked.
     */
    protected Position whereNeeded;

    /**
     * Unit picked for producing the trainee.
     */
    protected Unit producer;

    /**
     * Current state of this item.
     */
    protected ETrainingQueueItemStates state;

    /**
     * Copy constructor.
     * @param other original
     */
    public TrainingQueueItem(TrainingQueueItem other) {
        super(other);
        this.trainee = other.trainee;
        this.whereNeeded = other.whereNeeded;
        this.producer = other.producer;
        this.state = other.state;
    }

    /**
     * Constructor for when one doesn't care where the unit will be trained.
     * @param priority priority of the item in the training queue
     * @param trainee what unit to train
     */
    public TrainingQueueItem(EPriority priority, UnitType trainee) {
        super(priority);
        this.trainee = trainee;
        this.state = ETrainingQueueItemStates.UNQUEUED;
    }

    /**
     * Constructor for when one doesn't care where the unit will be trained.
     * @param priority priority of the item in the training queue
     * @param trainee what unit to train
     * @param whereNeeded position near which unit should be trained if possible
     */
    public TrainingQueueItem(EPriority priority, UnitType trainee, Position whereNeeded) {
        super(priority);
        this.trainee = trainee;
        this.whereNeeded = whereNeeded;
    }

    @Override
    public Cost getCost() {
        return new Cost(this.trainee.mineralPrice(), this.trainee.gasPrice());
    }

    public UnitType getTrainee() {
        return this.trainee;
    }

    public Position getWhereNeeded() {
        return this.whereNeeded;
    }

    public Unit getProducer() {
        return this.producer;
    }

    public ETrainingQueueItemStates getState() {
        return this.state;
    }

    public void setProducer(Unit producer) {
        this.producer = producer;
    }

    /**
     * Advances the item into the next state.
     * @return new state of the item
     */
    public ETrainingQueueItemStates advanceState() {
        return this.state = this.state.next();
    }

    /**
     * Cancels the production of the item.
     */
    public void cancel() {
        this.state = ETrainingQueueItemStates.CANCELED;
    }

    /**
     * Puts the item (back) into queued state if the item was already queued before. Otherwise the item is put into UNQUEUED state.
     */
    public void reset() {
        if (this.state.compareTo(ETrainingQueueItemStates.QUEUED) >= 0) {
            this.state = ETrainingQueueItemStates.QUEUED;
        }
        else {
            this.state = ETrainingQueueItemStates.UNQUEUED;
        }
    }

    /**
     * Starts the training of the trainee if possible.
     * @return True if the training has begun, false otherwise.
     */
    public boolean train() {
        if (this.producer != null) {
            return this.producer.train(this.trainee);
        }

        return false;
    }

    protected class ProhibitedStateTrasitionException extends Exception {

    }
}
