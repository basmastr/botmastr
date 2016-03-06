package botmastr.production.resources;

import botmastr.common.Cost;
import botmastr.common.EPriority;
import botmastr.common.PriorityQueueInsertCountedItem;
import botmastr.production.AProductionQueueItem;

/**
 * Represents a single request for resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class ResourcesRequest  extends PriorityQueueInsertCountedItem implements Comparable<ResourcesRequest> {

    /**
     * Amount of requested resources.
     */
    protected Cost resources;

    /**
     * Priority of the request. Reqest with higher priority will get accepted sooner in case of multiple requests.
     */
    protected EPriority priority;

    /**
     * Reference to object which initiated this request.
     */
    protected IResourcesRequestor requestor;
    //Probably rework rResourcesRequest to BuildingQueueItem, UpgradeQueueItem, TrainQueueItem implements IResourceRequest
//TODO UnitResourcesRequest + UpgradeResourcesRequest + AResourcesRequest or something else (prototype? or subclass UnitType and UpgradeType and make them implement an interface)
    /**
     * What are the resources for.
     */
    protected AProductionQueueItem reason;

    public ResourcesRequest(ResourcesRequest other) {
        super(other);
        this.resources = other.resources;
        this.priority = other.priority;
        this.requestor = other.requestor;
        this.reason = other.reason;
    }

    /**
     * Construtor for requests with known priority.
     * @param resources Amount of resources requested.
     * @param priority Priority of the request.
     * @param reason Reason for this request. Can be either UnitType or UpgradeType.
     * @param requestor
     */
    public ResourcesRequest(Cost resources, EPriority priority, AProductionQueueItem reason, IResourcesRequestor requestor) {
        this.resources = resources;
        this.priority = priority;
        this.reason = reason;
        this.requestor = requestor;
    }

    /**
     * Construtor for requests with unknown priority. Priority will be set to medium.
     * @param resources  Amount of resources requested.
     * @param reason Reason for this request. Can be either UnitType or UpgradeType.
     * @param requestor
     */
    public ResourcesRequest(Cost resources, AProductionQueueItem reason, IResourcesRequestor requestor) {
        this.resources = resources;
        this.priority = EPriority.MEDIUM;
        this.reason = reason;
        this.requestor = requestor;
    }

    /**
     * Sends the request to the ResourceManager.
     */
    public void send() {
        ResourceManager.getInstance().ask(this);
    }


    /**
     * Accepts the request, signalling the requestor to act on it.
     */
    public void accept() {
        this.requestor.requestAccepted(this);
    }

    /**
     * Deny the request, signalling the requestor to deal with it.
     */
    public void deny() {
        this.requestor.requestDenied(this);
    }

    public Cost getResources() {
        return this.resources;
    }

    public EPriority getPriority() {
        return this.priority;
    }

    public IResourcesRequestor getRequestor() {
        return this.requestor;
    }

    public AProductionQueueItem getReason() {
        return this.reason;
    }

    @Override
    public int compareTo(ResourcesRequest o) {
        final int ret = this.priority.compareTo(o.getPriority());

        if (ret == 0) {
            return super.compareTo(o);
        }

        return ret;
    }

}
