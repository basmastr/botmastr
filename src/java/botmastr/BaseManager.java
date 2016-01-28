package botmastr;

import bwapi.UnitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages owned bases.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BaseManager implements IManager{
    /**
     * Singleton instance.
     */
    private static final BaseManager INSTANCE = new BaseManager();

    /**
     * List of owned bases.
     */
    private List<MyBase> bases;

    /**
     * Private constructor because this is a singleton.
     */
    private BaseManager() {
    }

    public static BaseManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a base to the list of managed bases.
     * @param base Base to be added.
     */
    public void addBase(MyBase base) {
        this.bases.add(base);
    }

    @Override
    public void tic() {
        this.reassignWorkers();
    }


    private void reassignWorkers() {
        // TODO: 28.1.2016 temporary
        if (this.bases.size() > 0) {
            final MyBase base = this.bases.get(0);
            for (UnitData worker : UnitManager.getInstance().getUnitsByType(UnitManager.TYPES_WORKERS)) {
                base.assignWorker(worker);
            }
        }

    }
}
