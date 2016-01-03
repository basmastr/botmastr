package java.botmastr;

import java.util.List;
import java.util.stream.Collectors;

import bwapi.Mirror;

/**
 * Manager for mobile units.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class UnitManager extends ABaseManager {

    /**
     * Singleton intance.
     */
    private static final UnitManager INSTANCE = new UnitManager();


    /**
     * Unit list
     */
    protected List<UnitData> units;

    /**
     * Private constructor cos this is a singleton.
     */
    private UnitManager() {
    }

    public static UnitManager getInstance() {
        return INSTANCE;
    }


    /**
     * Initiates the unit list.
     * @param bwapi game state
     */
    @Override
    public void init(Mirror bwapi) {
        super.init(bwapi);
        this.units.addAll(this.bwapi.getGame().getAllUnits().stream().map(UnitData::new).collect(Collectors.toList()));
    }

    /**
     * Gets the list of owned military units.
     * @return List of owned military units.
     */
    public List<UnitData> getMilitaryUnits() {
        // TODO: 3.1.2016
        return this.units;
    }

    public List<UnitData> getUnits() {
        return this.units;
    }
}
