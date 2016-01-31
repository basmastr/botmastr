package botmastr;

import bwapi.*;

import java.util.Arrays;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BasicBotMain extends DefaultBWListener {
    private static BasicBotMain INSTANCE;
    private Mirror mirror;

    private BasicBotMain() {
        this.mirror = new Mirror();
        this.mirror.getModule().setEventListener(this);
        this.mirror.startGame();
    }

    public static void main(String[] args) {
        INSTANCE = new BasicBotMain();
    }

    @Override
    public void onStart() {
        Common.getInstance().init(this.mirror);
        UnitManager.getInstance().init(this.mirror);
        BaseManager.getInstance().init(this.mirror);
    }

    @Override
    public void onFrame() {
        this.mirror.getGame().drawBoxMap(800, 100, 1300, 450, Color.Purple);
        this.mirror.getGame().drawCircleMap(1984, 3792, 350, Color.Purple);

        UnitManager.getInstance().tic();
        BaseManager.getInstance().tic();
    }

    @Override
    public void onUnitComplete(Unit unit) {
        System.out.println("unit complete unit.getType() = " + unit.getType());
        UnitManager.getInstance().addUnit(unit);
    }


}
