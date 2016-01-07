package java.botmastr;

import bwapi.TilePosition;

import java.util.PriorityQueue;

/**
 * Takes care of keeping a list of buildings to build and actually building them.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BuildingManager extends AManager implements IResourcesRequestor, IManager {
    /**
     * Singleton instance.
     */
    private static final BuildingManager INSTANCE = new BuildingManager();
    /**
     * Queue of buildings to build next.
     */
    protected PriorityQueue<BuildQueueItem> queue;

    /**
     * Private because this is a singleton.
     */
    private BuildingManager() {
        this.queue = new PriorityQueue<>();
    }

    public static BuildingManager getInstance() {
        return INSTANCE;
    }

    /**
     * Request for recources has been accepted by the resource manager
     * @param request Request that has been accepted.
     */
    @Override
    public void requestAccepted(ResourcesRequest request) {
        //find place to build
        //find worker to build it
        //create UnitObjective
   }

    @Override
    public void requestDenied(ResourcesRequest request) {

    }

    @Override
    public void tic() {
        final BuildQueueItem item = this.queue.poll();
        if (item != null) {
            final ResourcesRequest request = new ResourcesRequest(item.getBuilding().mineralPrice(), item.getBuilding().gasPrice());
            request.send();
        }
    }

    // TODO: 7.1.2016  
    protected TilePosition getPlacement() {
    }
}
