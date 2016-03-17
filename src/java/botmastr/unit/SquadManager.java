package botmastr.unit;

import botmastr.common.AManager;
import botmastr.unit.objective.UnitObjectiveScout;
import bwapi.Mirror;
import bwapi.Unit;
import bwta.BWTA;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the actions of squads.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class SquadManager extends AManager {
    /**
     * Singleton intance.
     */
    private static final SquadManager INSTANCE = new SquadManager();

    /**
     * List of all currently existing squads.
     */
    private List<Squad> squads = new ArrayList<>();

    /**
     * Private constructor cos this is a singleton.
     */
    private SquadManager() {
    }

    public static SquadManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void init(Mirror bwapi) {
        super.init(bwapi);
    }

    @Override
    public void tic() {

//        for (Squad squad :
//                this.squads) {
//            if (!squad.hasObjective()) {
////                bwapi.getGame().isExplored();
//                squad.setObjective(new UnitObjectiveScout(BWTA.getStartLocations().stream().filter(p -> !p.getTilePosition().equals(this.bwapi.getGame().self().getStartLocation())).findFirst().get().getPosition()));
//                        //not working
////                        bwapi.getGame().enemy().getStartLocation().toPosition())
//            }
//
//            squad.tic();
//        }
    }

    /**
     * // TODO: 9.3.2016 TEMPORARY
     * @return
     */
    public Squad getSquad(Unit unit) {
        if (this.squads.isEmpty()) {
            this.squads.add(new Squad());
        }

        return this.squads.get(0);
    }
}
