package botmastr.common;

import botmastr.production.resources.ResourceManager;
import botmastr.unit.UnitData;
import botmastr.unit.UnitManager;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.UnitType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class Common  extends AManager {

    /**
     * Singleton instance.
     */
    private static final Common INSTANCE = new Common();

    /**
     * TODO put into Common?
     * Contains all UnitTypes that are mineral fields.
     */
    public static List<UnitType> TYPES_MINERALS;

    /**
     * TODO put into Common?
     * Contains all UnitTypes that are workers.
     */
    public static List<UnitType> TYPES_WORKERS;

    /**
     * Print debugging info?
     */
    protected boolean debug = true;

    /**
     * Private constructor cos this is a singleton.
     */
    private Common() {
    }

    /**
     * Initate the manager with game state.
     * @param bwapi game state
     */
    public void init(Mirror bwapi) {
        this.bwapi = bwapi;
        TYPES_MINERALS = Arrays.asList(
                UnitType.Resource_Mineral_Field, UnitType.Resource_Mineral_Field_Type_2, UnitType.Resource_Mineral_Field_Type_3);
        TYPES_WORKERS = Arrays.asList(
                UnitType.Terran_SCV, UnitType.Protoss_Probe, UnitType.Zerg_Drone);
    }

    @Override
    public void tic() {
        if (this.debug) {
            UnitManager.getInstance().getUnits().values().forEach(UnitData::debug);
            ResourceManager.getInstance().debug();
//            stream().map(IDebuggable.class::cast).forEach(IDebuggable::debug);
        }
    }

    public static Common getInstance() {
        return INSTANCE;
    }

    public Mirror getBwapi() {
        return this.bwapi;
    }

    public Game getGame() {
        return this.bwapi.getGame();
    }

    public boolean debug() {
        return this.debug;
    }
}
