package botmastr;

import java.util.PriorityQueue;

/**
 * Allocates resources to incoming requests for resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class ResourceManager extends AManager implements IDebuggable {
    /**
     * Singleton instance.
     */
    private static final ResourceManager INSTANCE = new ResourceManager();

    /**
     * Stores all awaiting requests for resources.
     */
    protected PriorityQueue<ResourcesRequest> requests;

    protected Integer promisedMinerals = 0;
    protected Integer promisedGas = 0;

    /**
     * Private constructor because this is a singleton.
     */
    private ResourceManager() {
        this.requests = new PriorityQueue<>();
    }

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void tic() {
        //grant resources for top priority request if we have enough
        if (!this.requests.isEmpty()) {
            final ResourcesRequest item = this.requests.peek();
            if (hasSufficientResources(item)) {
                acceptRequest(item);
                this.requests.poll();
            }
        }
    }

    @Override
    public void debug() {
        Common.getInstance().getGame().drawTextScreen(410, 20, "Mins: " + getEffectiveMinerals() + "\t\tGas: " + getEffectiveGas());
    }

    /**
     * Adds the supplied request into the queue.
     * @param request Request to be added into the resources request queue.
     */
    public void ask(ResourcesRequest request) {
        this.requests.add(request);
    }

    /**
     * Allocate resources for the request.
     * @param request Request that's to be allocated resources.
     */
    protected void acceptRequest(ResourcesRequest request) {
        request.accept();
        this.requestAccepted(request);
    }

    /**
     * Signals that the resources were allocated for the request and we need to keep track of them as promised resources.
     * @param request Request that was accepted.
     */
    protected void requestAccepted(ResourcesRequest request) {
        this.promisedMinerals += request.getMinerals();
        this.promisedGas += request.getGas();
    }

    /**
     * Signals that the resources allocated for request were actually spent and they can be deducted from the promised resources.
     * @param request Cost of the materialized request.
     */
    public void requestMaterialized(Cost request) {
        this.promisedMinerals -= request.getMinerals();
        this.promisedGas -= request.getGas();
    }

    /**
     * Checks weather the player currently has enough resources to accept the request.
     * @param request Request that needs resources.
     * @return True if there are enough resources, false otherwise.
     */
    protected boolean hasSufficientResources(ResourcesRequest request) {
        return getEffectiveMinerals() >= request.getMinerals() && getEffectiveGas() >= request.getGas();
    }

    /**
     * Returns player's minerals deducted by minerals promised to previous, not yet materialized, requests.
     * @return Current minerals minus promised minerals.
     */
    protected Integer getEffectiveMinerals() {
        return this.bwapi.getGame().self().minerals() - promisedMinerals;
    }
    /**
     * Returns player's gas deducted by gas promised to previous, not yet materialized, requests.
     * @return Current gas minus promised gas.
     */
    protected Integer getEffectiveGas() {
        return this.bwapi.getGame().self().gas() - promisedGas;
    }
}
