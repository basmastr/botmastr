package botmastr;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Takes care of keeping a list of buildings to build and actually building them.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BuildingManager extends AManager implements IResourcesRequestor {

    /**
     * Singleton instance.
     */
    private static final BuildingManager INSTANCE = new BuildingManager();

    /**
     * Queue of buildings to build next.
     */
    protected PriorityQueueInsertOrdered<BuildingQueueItem> queue = new PriorityQueueInsertOrdered<>();

    /**
     * List of owned buildings.
     */
    protected List<UnitType> buildings;

    /**
     * Private because this is a singleton.
     */
    private BuildingManager() {
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
        BuildingQueueItem item = (BuildingQueueItem) request.getReason();
        item.setState(EBuildingQueueItemStates.AWAITING_WORKER_ALLOCATION);
        final TilePosition aroundPosition = this.bwapi.getGame().self().getStartLocation();
        final UnitData builder = getBuilder(aroundPosition);
        if (builder == null) {
            item.setState(EBuildingQueueItemStates.QUEUED);
            return;
        }
        else {
            final TilePosition position = getPlacement(builder.getUnit(), item.getBuilding(), aroundPosition);
            if (position == null) {
                return;
            }
            builder.addObjective(new UnitObjectiveMove(builder, position.toPosition(), EPriority.HIGH));
            builder.addObjective(new UnitObjectiveBuild(builder, item, position, EPriority.HIGH));
//            this.queue.remove(item);
            // TODO: 22.2.2016 very temporary, need to ensure completing of the queueItem
        }

        //find place to build
        //find worker to build it
   }

    @Override
    public void requestDenied(ResourcesRequest request) {
        BuildingQueueItem item = (BuildingQueueItem) request.getReason();
        item.setState(EBuildingQueueItemStates.QUEUED);
    }

    @Override
    public void tic() {
        final PriorityQueueInsertOrdered<BuildingQueueItem> queued = getQueueItemsByState(EBuildingQueueItemStates.QUEUED);
        if (!queued.isEmpty()) {
            final BuildingQueueItem item = queued.peek();
            final ResourcesRequest request = new ResourcesRequest(item.getBuilding().mineralPrice(), item.getBuilding().gasPrice(), item, this);
            request.send();
            item.setState(EBuildingQueueItemStates.AWAITING_RESOURCES_ALLOCATION);
        }

        final PriorityQueueInsertOrdered<BuildingQueueItem> building = getQueueItemsByState(EBuildingQueueItemStates.BUILDING);
        if (building.size() > 0) {
            System.out.println("something is building");
        }
        building.forEach(i -> ResourceManager.getInstance().requestMaterialized(i.getCost()));
        this.queue.removeAll(building);
    }

    /**
     *
     * @return
     */
    protected UnitData getBuilder(TilePosition where) {
        final List<UnitData> workers = UnitManager.getInstance().getUnitsByType(Common.TYPES_WORKERS);
        if (workers.size() > 0) {
            return workers.get(0);
        }

        return null;
    }

    /**
     *
     * @param state
     * @return
     */
    private PriorityQueueInsertOrdered<BuildingQueueItem> getQueueItemsByState(EBuildingQueueItemStates state) {
        //constructor for PQIO with setting of the numberInserted
        return this.queue.stream().filter(i -> i.getState().equals(state)).collect(Collectors.toCollection(PriorityQueueInsertOrdered::new));
    }

    /**
     * Finds a suitable position for a building.
     * @param builder Unit building this building
     * @param building What type of building we need a placement for.
     * @param aroundTile What type of building we need a placement for.
     * @return
     */
    protected TilePosition getPlacement(Unit builder, UnitType building, TilePosition aroundTile) {
        TilePosition ret = null;
        int maxDist = 3;
        final int stopDist = 30;
//todo get UnitType.Resource_Vespene_Geyser closest to aroundTile
        // Refinery, Assimilator, Extractor
        if (building.isRefinery()) {
            for (Unit n : bwapi.getGame().getNeutralUnits()) {
                if ((n.getType() == UnitType.Resource_Vespene_Geyser) &&
                        ( Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist ) &&
                        ( Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist )) {
                    return n.getTilePosition();
                }
            }
        }

        while ((maxDist < stopDist) && (ret == null)) {
            for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
                for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
                    if (bwapi.getGame().canBuildHere(new TilePosition(i,j), building, builder, false)) {
                        // units that are blocking the tile
                        boolean unitsInWay = false;
                        for (Unit u : bwapi.getGame().getAllUnits()) {
                            if (u.getID() == builder.getID()) {
                                continue;
                            }
                            if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) {
                                unitsInWay = true;
                            }
                        }
                        if (!unitsInWay) {
                            return new TilePosition(i,j);
                        }
                        // creep for Zerg
                        if (building.requiresCreep()) {
                            boolean creepMissing = false;
                            for (int k=i; k<=i+building.tileWidth(); k++) {
                                for (int l=j; l<=j+building.tileHeight(); l++) {
                                    if (!bwapi.getGame().hasCreep(new TilePosition(k, l))) {
                                        creepMissing = true;
                                    }
                                    break;
                                }
                            }
                            if (creepMissing) {
                                continue;
                            }
                        }

                        if (building.requiresPsi()) {
                            boolean creepMissing = false;
                            for (int k=i; k<=i+building.tileWidth(); k++) {
                                for (int l=j; l<=j+building.tileHeight(); l++) {
                                    if (!bwapi.getGame().hasPower(new TilePosition(k, l))) {
                                        creepMissing = true;
                                    }
                                    break;
                                }
                            }
                            if (creepMissing) {
                                continue;
                            }
                        }
                    }
                }
            }
            maxDist += 2;
        }

        if (ret == null) {
            bwapi.getGame().printf("Unable to find suitable build position for " + building.toString());
        }
        return ret;
    }

    public void addQueueItem(BuildingQueueItem item) {
        this.queue.add(item);
        item.setState(EBuildingQueueItemStates.QUEUED);
    }
}
