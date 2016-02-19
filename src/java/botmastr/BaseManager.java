package botmastr;

import bwapi.Color;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages owned bases.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BaseManager extends AManager implements IManager{
    /**
     * Singleton instance.
     */
    private static final BaseManager INSTANCE = new BaseManager();

    /**
     * List of owned bases.
     */
    private List<MyBase> bases = new ArrayList<>();

    private boolean moving;

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
        final int updateInterval = 40;
        if (this.bases.size() > 0) {
            final MyBase base = this.bases.get(0);
            base.refreshResources();
            base.getWorkers().clear();

            for (UnitData worker : UnitManager.getInstance().getUnitsByType(Common.TYPES_WORKERS)) {
                base.assignWorker(worker);
                if(!moving){
                    System.out.println("Moving added");
                    worker.getPlan().clear();
                    worker.addObjective(new UnitObjectiveMove(worker, base.getGeysers().iterator().next().getPosition(), EPriority.HIGH));
                    worker.addObjective(new UnitObjectiveBuild(worker, UnitType.Protoss_Assimilator, base.getGeysers().iterator().next().getTilePosition(), EPriority.HIGH));
                    moving = true;
                }
            }


            base.debug();
        }
//        if (this.bwapi.getGame().getFrameCount() % updateInterval == 0) { //only update at a certain interval
//            // TODO: 15.2.2016 go through bases and call base's tic method
//            this.releaseWorkers();
//            this.reassignWorkers();
//        }
    }


    private void resourcesDebug() {
        for (MyBase b :
                this.bases) {
            for (Unit u :
                    b.getMineralPatches()) {
                this.bwapi.getGame().drawDotMap(u.getX(), u.getY(), Color.Red);
                System.out.println("u.getID() = " + u.getID());
            }
        }
    }

    /**
     * Add vespene geyser unit to a base if there is one in the necessary threshold
     * @see MyBase.RESOURCES_THRESHOLD
     * @param unit
     */
    public void addGeyser(Unit unit) {
        for (MyBase b: this.bases) {
            if (b.main.getUnit().getDistance(unit) <= MyBase.RESOURCES_THRESHOLD) {
                b.addGeyser(unit);
            }
        }
    }

    /**
     * Add vespene mineral unit to a base if there is one in the necessary threshold
     * @see MyBase.RESOURCES_THRESHOLD
     * @param unit
     */
    public void addMineralPatch(Unit unit) {
        for (MyBase b: this.bases) {
            if (b.main.getUnit().getDistance(unit) <= MyBase.RESOURCES_THRESHOLD) {
                b.addMineralPatch(unit);
            }
        }
    }

    private void releaseWorkers() {
        if (this.bases.size() > 0) {
            final MyBase base = this.bases.get(0);
            base.getWorkers().clear();
        }
    }


    private void reassignWorkers() {
        // TODO: 28.1.2016 temporary
        if (this.bases.size() > 0) {
            final MyBase base = this.bases.get(0);
            base.refreshResources();
//            System.out.println("UnitManager.getInstance().getUnitsByType(UnitManager.TYPES_WORKERS) = " + UnitManager.getInstance().getUnitsByType(Common.TYPES_WORKERS).size());
            for (UnitData worker : UnitManager.getInstance().getUnitsByType(Common.TYPES_WORKERS)) {
                base.assignWorker(worker);
            }
        }

    }
}
