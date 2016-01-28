package botmastr;

import java.util.List;
import java.util.PriorityQueue;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Takes care of keeping a list of buildings to build and actually building them.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BuildingManager extends AManager implements IResourcesRequestor, IManager {

    /**
     * Singleton instance.
     */
    private static final BuildingManager INSTANCE = new BuildingManager();

    /**
     * Queue of buildings to build next.
     */
    protected PriorityQueue<BuildingQueueItem> queue;

    /**
     * List of owned buildings.
     */
    protected List<UnitType> buildings;

    /**
     * Private because this is a singleton.
     */
    private BuildingManager() {
        this.queue = new PriorityQueue<>();
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
        //find place to build
        //find worker to build it
        //create UnitObjectiveMineMinerals
   }

    @Override
    public void requestDenied(ResourcesRequest request) {

    }

    @Override
    public void tic() {
        final BuildingQueueItem item = this.queue.poll();
        if (item != null) {
            final ResourcesRequest request = new ResourcesRequest(item.getBuilding().mineralPrice(), item.getBuilding().gasPrice(), item);
            request.send();
        }
    }
//
//    /**
//     *
//     * @return
//     */
//    protected UnitData getBuilder(TilePosition where) {
//
//    }

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
                    if (bwapi.getGame().canBuildHere(builder, new TilePosition(i,j), building, false)) {
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
}
