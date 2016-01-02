import bwapi.Mirror;
import bwapi.Unit;

import java.util.List;

/**
 * Created by Tomas on 20.12.2015.
 */
public class UnitManager {
    protected Mirror bwapi;
    protected List<UnitData> units;

    public UnitManager(Mirror bwapi) {
        this.bwapi = bwapi;
        initUnits();
    }

    protected void initUnits() {
        for(Unit u: bwapi.getGame().getAllUnits()) {
            units.add(new UnitData(u));
        }
    }

    public List<UnitData> getUnits() {
        return units;
    }
}
