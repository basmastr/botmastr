package botmastr.base;

import java.util.*;
import java.util.stream.Collectors;

import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import botmastr.unit.UnitManager;
import botmastr.unit.objective.UnitObjectiveMineMinerals;
import bwapi.Color;
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
    private Set<UnitData> workers = new HashSet<>();

    /**
     *
     */
    private Set<Unit> mineralPatches = new HashSet<>();

    /**
     *
     */
    private Set<Unit> geysers = new HashSet<>();

    /**
     *
     * @param main
     * @param baseLocation
     */
    public MyBase(UnitData main) {
        super(main);
        final Collection<Unit> mins = main.getUnit().getUnitsInRadius(RESOURCES_THRESHOLD).stream().filter(u -> Common.TYPES_MINERALS.contains(u.getType())).collect(Collectors.toList());
        this.mineralPatches.addAll(mins);
        this.geysers.addAll(UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser));
        BaseManager.getInstance().addBase(this);
    }

    /**
     * Prints debugging information for this base.
     */
    public void debug() {
        // TODO: 15.2.2016 put this.main.getUnit().getX(), this.main.getUnit().getY() into UnitData?
        //resources treshhold
        Common.getInstance().getGame().drawCircleMap(this.main.getUnit().getX(), this.main.getUnit().getY(), RESOURCES_THRESHOLD, Color.Purple);
        //resources
        Common.getInstance().getGame().drawTextMap(this.main.getUnit().getX(), this.main.getUnit().getY(), Integer.toString(this.mineralPatches.size()));
        Common.getInstance().getGame().drawTextMap(this.main.getUnit().getX(), this.main.getUnit().getY()+15, Integer.toString(this.geysers.size()));
        Common.getInstance().getGame().drawTextMap(this.main.getUnit().getX(), this.main.getUnit().getY()+30, Integer.toString(this.workers.size()));

        for (Unit u :
                this.mineralPatches) {
            Common.getInstance().getGame().drawCircleMap(u.getX(), u.getY(), 30, Color.Red);
        }

        for (Unit u :
                this.geysers) {
            Common.getInstance().getGame().drawCircleMap(u.getX(), u.getY(), 30, Color.Red);
        }
    }
    /**
     * Adds a worker under management of this base.
     * @param worker Worker to be added.
     */
    public void assignWorker(UnitData worker) {
        this.workers.add(worker);
//        System.out.println("this.workers.size() = " + this.workers.size());
//        System.out.println("this.mineralPatches.size() = " + this.mineralPatches.size());
//        System.out.println("this.geysers.size() = " + this.geysers.size());
        // TODO: 28.1.2016 temporary
        if (!this.mineralPatches.isEmpty()){
            worker.addObjective(new UnitObjectiveMineMinerals(worker, this.mineralPatches.iterator().next(), EPriority.MEDIUM));
        }
    }

    public Set<UnitData> getWorkers() {
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

    /**
     * Reloads base's mineral patches and geysers.
     */
    public void refreshResources() {
        this.geysers.clear();
        this.mineralPatches.clear();
        this.geysers.addAll(UnitManager.getInstance().getUnitsInRadius(this.main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser));
        this.mineralPatches.addAll(UnitManager.getInstance().getUnitsInRadius(this.main.getUnit().getPosition(), RESOURCES_THRESHOLD,  Common.TYPES_MINERALS));
    }


    public Set<Unit> getGeysers() {
        return this.geysers;
    }

    public Set<Unit> getMineralPatches() {
        return this.mineralPatches;
    }

}
