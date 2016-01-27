package botmastr;

import bwta.BWTA;
import bwta.BaseLocation;

/**
 * Represents a single.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class ABase {
    /**
     * Main building (Nexus/Hatchery/CommandCenter) of this base.
     */
    protected UnitData main;

    /**
     * BaseLocation corresponding to this base.
     * @see BaseLocation
     */
    protected BaseLocation baseLocation;

    /**
     *
     * @param main
     */
    public ABase(UnitData main) {
        this.main = main;
        this.baseLocation = BWTA.getNearestBaseLocation(main.getUnit().getPosition());
    }
}
