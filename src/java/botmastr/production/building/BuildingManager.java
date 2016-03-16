package botmastr.production.building;

import java.util.List;
import java.util.stream.Collectors;

import botmastr.common.*;
import botmastr.production.resources.IResourcesRequestor;
import botmastr.production.resources.ResourceManager;
import botmastr.production.resources.ResourcesRequest;
import botmastr.unit.UnitData;
import botmastr.unit.UnitManager;
import botmastr.unit.objective.UnitObjectiveBuild;
import botmastr.unit.objective.UnitObjectiveMove;
import bwapi.Position;
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
    protected PriorityQueueInsertCounted<BuildingQueueItem> queue = new PriorityQueueInsertCounted<>();

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
//        final TilePosition aroundPosition = this.bwapi.getGame().self().getStartLocation();
//        final UnitData builder = getBuilder(aroundPosition);
//        if (builder == null) {
//            return;
//        }
//
//        item.setState(EBuildingQueueItemStates.AWAITING_PLACEMENT_ALLOCATION);
//
//        final TilePosition position = getPlacement(builder.getUnit(), item.getBuilding(), aroundPosition);
//        if (position == null) {
//            return;
//        }
//        builder.addObjective(new UnitObjectiveMove(builder, position.toPosition(), EPriority.HIGH));
//        builder.addObjective(new UnitObjectiveBuild(builder, item, position, EPriority.HIGH));
//            this.queue.remove(item);
        // TODO: 22.2.2016 very temporary, need to ensure completing of the queueItem
   }

    @Override
    public void requestDenied(ResourcesRequest request) {
        final BuildingQueueItem item = (BuildingQueueItem) request.getReason();
        item.setState(EBuildingQueueItemStates.QUEUED);
    }

    @Override
    public void tic() {
        final PriorityQueueInsertCounted<BuildingQueueItem> queued = getQueueItemsByState(EBuildingQueueItemStates.QUEUED);
        if (!queued.isEmpty()) {
            final BuildingQueueItem item = queued.peek();
            final Cost resources = new Cost(item.getBuilding().mineralPrice(), item.getBuilding().gasPrice());
            final ResourcesRequest request = new ResourcesRequest(resources, item.getPriority(), item, this);
            request.send();
            item.setState(EBuildingQueueItemStates.AWAITING_RESOURCES_ALLOCATION);
        }

        if (Common.getInstance().checkForUpdateInterval(30)) {
            handleAwaitingWorkerAllocation();
        }

        final PriorityQueueInsertCounted<BuildingQueueItem> building = getQueueItemsByState(EBuildingQueueItemStates.BUILDING);
        building.forEach(i -> ResourceManager.getInstance().requestMaterialized(i.getCost()));
        this.queue.removeAll(building);
    }

    protected void handleAwaitingWorkerAllocation() {
        final PriorityQueueInsertCounted<BuildingQueueItem> items = getQueueItemsByState(EBuildingQueueItemStates.AWAITING_WORKER_ALLOCATION);

        for (BuildingQueueItem item :
                items) {
            final TilePosition aroundPosition = this.bwapi.getGame().self().getStartLocation();
            final UnitData builder = getBuilder(aroundPosition);

            if (builder == null) {
                return;
            }

            final TilePosition position = getPlacement(builder.getUnit(), item.getBuilding(), aroundPosition);

            if (position == null) {
                return;
            }

            builder.addObjective(new UnitObjectiveMove(builder, position.toPosition(), EPriority.HIGH));
            builder.addObjective(new UnitObjectiveBuild(builder, item, position, EPriority.HIGH));
            item.setState(EBuildingQueueItemStates.WORKER_EN_ROUTE);
        }
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
    private PriorityQueueInsertCounted<BuildingQueueItem> getQueueItemsByState(EBuildingQueueItemStates state) {
        return this.queue.stream().filter(i -> i.getState().equals(state)).collect(Collectors.toCollection(PriorityQueueInsertCounted::new));
    }

    /**
     * Finds a suitable position for a building.
     * @param builder Unit building this building
     * @param building What type of building we need a placement for.
     * @param aroundTile What type of building we need a placement for.
     * @return
     * @author inspired by Plankton's Loki bot
     */
    protected TilePosition getPlacement(Unit builder, UnitType building, TilePosition aroundTile) {
        TilePosition buildTile = null;
        int maxDist = 3;
        final int stopDist = 30;
//todo get UnitType.Resource_Vespene_Geyser closest to aroundTile
        // Refinery, Assimilator, Extractor
        if (building.isRefinery()) {
            for (Unit n : bwapi.getGame().getNeutralUnits()) {
                if ((n.getType() == UnitType.Resource_Vespene_Geyser) &&
                        (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist) &&
                        (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist)) {
                    buildTile = n.getTilePosition();
                }
            }
        }

        while ((maxDist < stopDist) && (buildTile == null)) {
            for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
                for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
                    final TilePosition potentialBuildTile = new TilePosition(i, j);

                    if (bwapi.getGame().canBuildHere(potentialBuildTile, building, builder, false)) {
                        // are units that are blocking the tile? canBuildHere check doesn't work always
                        final Position bottomRight =  new Position(potentialBuildTile.toPosition().getX() + building.width() + 32, potentialBuildTile.toPosition().getY() + building.height() + 32);
                        final List<Unit> units = bwapi.getGame().getUnitsInRectangle(potentialBuildTile.toPosition(), bottomRight);
                        if (units.isEmpty() || (units.size() == 1 && units.get(0).getID() == builder.getID())) {
                            buildTile = potentialBuildTile;
                        }
                    }
                }
            }

            maxDist += 2;
        }

        if (buildTile == null) {
            bwapi.getGame().printf("Unable to find suitable build position for " + building.toString());
        }
        return buildTile;
    }

    public void addQueueItem(BuildingQueueItem item) {
        this.queue.add(item);
        item.setState(EBuildingQueueItemStates.QUEUED);
    }
}
