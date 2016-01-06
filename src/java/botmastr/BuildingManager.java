package java.botmastr;

import java.util.PriorityQueue;

/**
 * Takes care of keeping a list of buildings to build and actually building them.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BuildingManager {
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
    }

    public static BuildingManager getInstance() {
        return INSTANCE;
    }

}
