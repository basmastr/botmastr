package botmastr.base;

import java.util.*;
import java.util.stream.Collectors;

import botmastr.base.mining.BasicMiningStrategy;
import botmastr.base.mining.IMiningStrategy;
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
    public static final Integer MAX_WORKERS_PER_GAS = 3;
    // TODO: 24.2.2016 study effective mineral mining
    public static final Integer MAX_WORKERS_PER_MINERAL = 2;

    /**
     * Workers attached to this base.
     */
    protected Set<UnitData> workers = new HashSet<>();

    /**
     * Mineral patches in the region of this base.
     */
    protected Set<Unit> mineralPatches = new HashSet<>();

    /**
     *
     */
    protected Set<Unit> geysers = new HashSet<>();

    protected IMiningStrategy miningStrategy;

    /**
     *
     * @param main
     */
    public MyBase(UnitData main) {
        super(main);
        this.miningStrategy = new BasicMiningStrategy();
        this.mineralPatches.addAll(main.getUnit().getUnitsInRadius(RESOURCES_THRESHOLD).stream().filter(u -> Common.TYPES_MINERALS.contains(u.getType())).collect(Collectors.toList()));
        this.geysers.addAll(UnitManager.getInstance().getUnitsInRadius(main.getUnit().getPosition(), RESOURCES_THRESHOLD, UnitType.Resource_Vespene_Geyser));
        BaseManager.getInstance().addBase(this);
    }


    public void tic() {
        this.miningStrategy.tic(this.workers, this.mineralPatches, getRefineries());
        debug();
    }

    /**
     * Maps geysers of the base to refineries (if they are built) and returns them in a set.
     * @return Set of completed refineries belonging to this base.
     */
    protected Set<Unit> getRefineries() {
        return this.geysers.stream().filter(o -> o.getType().isRefinery() && o.isCompleted()).collect(Collectors.toSet());
    }

    /**
     * Prints debugging information for this base.
     */
    public void debug() {
        if (Common.getInstance().debug()) {
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
    }
    /**
     * Adds a worker under management of this base.
     * @param worker Worker to be added.
     */
    public void assignWorker(UnitData worker) {
        this.workers.add(worker);
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
        this.geysers.addAll(UnitManager.getInstance().getUnitsInRadius(this.main.getUnit().getPosition(), RESOURCES_THRESHOLD, Common.TYPES_REFINERIES));
        this.mineralPatches.addAll(UnitManager.getInstance().getUnitsInRadius(this.main.getUnit().getPosition(), RESOURCES_THRESHOLD,  Common.TYPES_MINERALS));
    }


    public Set<Unit> getGeysers() {
        return this.geysers;
    }

    public Set<Unit> getMineralPatches() {
        return this.mineralPatches;
    }

}
