package botmastr;

import bwapi.DefaultBWListener;
import bwapi.Mirror;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class BasicBotMain extends DefaultBWListener {
    private static final BasicBotMain INSTANCE = new BasicBotMain();
    private Mirror mirror;

    private BasicBotMain() {
        this.mirror = new Mirror();
        this.mirror.getModule().setEventListener(this);
        this.mirror.startGame();
    }
    @Override
    public void onUnitCreate(Unit unit) {
        UnitManager.getInstance().addUnit(unit);
    }


}
