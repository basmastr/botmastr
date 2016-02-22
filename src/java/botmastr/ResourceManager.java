package botmastr;

import java.util.PriorityQueue;

/**
 * Allocates resources to incoming requests for resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class ResourceManager extends AManager implements IManager{
    /**
     * Singleton instance.
     */
    private static final ResourceManager INSTANCE = new ResourceManager();

    /**
     * Stores all awaiting requests for resources.
     */
    private PriorityQueue<ResourcesRequest> requests;

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
                item.accept();
                this.requests.poll();
            }
        }
    }

    /**
     * Adds the supplied request into the queue.
     * @param request Request to be added into the resources request queue.
     */
    public void ask(ResourcesRequest request) {
        this.requests.add(request);
    }

    /**
     * Accepts the request, signalling the requestor to act on it.
     * @param request Request to be accepted.
     */
    protected void acceptRequest(ResourcesRequest request) {
        request.accept();
    }

    /**
     * Denies the request, signalling the requestor to deal with it.
     * @param request Request to be denied.
     */
    protected void denyRequest(ResourcesRequest request) {
        request.deny();
    }

    /**
     * Checks weather the player currently has enough resources to accept the request.
     * @param request Request that needs resources.
     * @return True if there are enough resources, false otherwise.
     */
    protected boolean hasSufficientResources(ResourcesRequest request) {
        return this.bwapi.getGame().self().minerals() >= request.getMinerals() && this.bwapi.getGame().self().gas() >= request.getGas();
    }
}
