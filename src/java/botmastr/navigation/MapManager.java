package botmastr.navigation;

import botmastr.common.AManager;
import botmastr.common.Common;
import botmastr.unit.Squad;
import bwapi.Mirror;
import bwapi.Region;
import bwapi.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the actions of squads.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class MapManager extends AManager {
    /**
     * Singleton intance.
     */
    private static final MapManager INSTANCE = new MapManager();

    /**
     * Private constructor cos this is a singleton.
     */
    private MapManager() {
    }

    public static MapManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void init(Mirror bwapi) {
        super.init(bwapi);
    }

    @Override
    public void tic() {
    }

    

    public Integer getRegionInfluence(Region region) {
        return region.getUnits().stream().mapToInt(this::evalUnitsInfluence).sum();
    }


    protected int evalUnitsInfluence(Unit unit) {
        if (unit.getPlayer().isEnemy(Common.getInstance().getPlayer())) {
            return unit.getType().buildScore();
        }
        else {
            return -unit.getType().buildScore();
        }
    }
}
