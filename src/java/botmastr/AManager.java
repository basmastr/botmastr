package botmastr;

import bwapi.Mirror;

/**
 * Abstract base class for all managers.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class AManager {

    /**
     * Game state
     */
    protected Mirror bwapi;

    /**
     * Initate the manager with game state.
     * @param bwapi game state
     */
    public void init(Mirror bwapi) {
        this.bwapi = bwapi;
    }

    /**
     * Method which runs every frame and executes all the managing stuff.
     */
    public abstract void tic();
}
