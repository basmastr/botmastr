package botmastr;

import botmastr.common.AManager;
import botmastr.unit.UnitManager;
import bwapi.Position;
import bwta.BWTA;

/**
 * Makes top level decisions about what to do.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class MainManager extends AManager {
    /**
     * Singleton instance.
     */
    private static final MainManager INSTANCE = new MainManager();

    protected boolean scouted;
    /**
     * Private because this is a singleton.
     */
    private MainManager() {}

    public static MainManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void tic() {
//        if (bwapi.getGame().elapsedTime() > 30 && !this.scouted) {
//            final Position opponentStart = BWTA.getStartLocations().stream().filter(p -> !p.getTilePosition().equals(this.bwapi.getGame().self().getStartLocation())).findFirst().get().getPosition();
//            UnitManager.getInstance().orderScouting(opponentStart);
//            this.scouted = true;
//        }
    }
}
