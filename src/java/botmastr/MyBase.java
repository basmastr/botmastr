package botmastr;

import java.util.*;

import bwapi.Unit;
import bwapi.UnitType;

/**
 * Represents a single owned base.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class MyBase extends ABase {
    /**
     *
     */
    public static final Integer RESOURCES_THRESHOLD = 400;

    /**
     * Workers attached to this base.
     */
    private List<UnitData> workers = new ArrayList<>();

    /**
     *
     */
    private List<Unit> mineralPatches = new ArrayList<>();

    /**
     *
     */
    private List<Unit> geysers = new ArrayList<>();

    /**
     *
     * @param main
     * @param baseLocation
     */
    public MyBase(UnitData main) {
        super(main);
//        System.out.println("main.getUnit().getUnitsInRadius(350) = " + main.getUnit().getUnitsInRadius(350));
//        this.mineralPatches = UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, Common.TYPES_MINERALS);
        this.mineralPatches.addAll(UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, Common.TYPES_MINERALS));
        this.geysers.addAll(UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser));
//        System.out.println("BaseCreation geysers found = " + UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser));
//        System.out.println("BaseCreation geysers total = " + this.geysers.size());
        BaseManager.getInstance().addBase(this);
    }

    /**
     * Adds a worker under management of this base.
     * @param worker Worker to be added.
     */
    public void assignWorker(UnitData worker) {
        this.workers.add(worker);
        System.out.println("this.workers.size() = " + this.workers.size());
        System.out.println("this.mineralPatches.size() = " + this.mineralPatches.size());
        System.out.println("this.geysers.size() = " + this.geysers.size());
        // TODO: 28.1.2016 temporary
        if (!this.mineralPatches.isEmpty()){
            worker.addObjective(new UnitObjectiveMineMinerals(this.mineralPatches.get(0), EPriority.MEDIUM));
        }
    }

    public List<UnitData> getWorkers() {
        return this.workers;
    }

    /**
     *
     * @param geyser
     */
    public void addGeyser(Unit geyser) {
        this.geysers.add(geyser);
    }

    /**
     *
     * @param mineral
     */
    public void addMineralPatch(Unit mineral) {
        this.mineralPatches.add(mineral);
    }
}
