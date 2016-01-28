package botmastr;

import java.util.ArrayList;
import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

/**
 * Represents a single owned base.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class MyBase extends ABase {
    /**
     *
     */
    private static final Integer RESOURCES_THRESHOLD = 350;

    /**
     * Workers attached to this base.
     */
    private List<UnitData> workers;

    /**
     *
     */
    private List<Unit> mineralPatches;

    /**
     *
     */
    private List<Unit> geysers;

    /**
     *
     * @param main
     * @param baseLocation
     */
    public MyBase(UnitData main) {
        super(main);
        this.workers = new ArrayList<>();
        this.mineralPatches = UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitManager.TYPES_MINERALS);
        this.geysers = UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser);
        BaseManager.getInstance().addBase(this);
    }

    /**
     * Adds a worker under management of this base.
     * @param worker Worker to be added.
     */
    public void assignWorker(UnitData worker) {
        this.workers.add(worker);
        // TODO: 28.1.2016 temporary
        worker.addObjective(new UnitObjectiveMineMinerals(this.mineralPatches.get(0)));
    }
}
