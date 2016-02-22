package botmastr;

import bwapi.*;
import bwta.BWTA;

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
        BWTA.readMap();
        BWTA.analyze();
        Common.getInstance().init(this.mirror);
        UnitManager.getInstance().init(this.mirror);
        BaseManager.getInstance().init(this.mirror);
        ResourceManager.getInstance().init(this.mirror);
        BuildingManager.getInstance().init(this.mirror);
        this.buildOrder1();
    }


    private void buildOrder1() {
        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Assimilator));
        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Pylon));
    }

    @Override
    public void onFrame() {
//        this.mirror.getGame().drawBoxMap(800, 100, 1300, 450, Color.Purple);
//        this.mirror.getGame().drawCircleMap(1984, 3792, 350, Color.Purple);
        UnitManager.getInstance().tic();
        BaseManager.getInstance().tic();
        ResourceManager.getInstance().tic();
        BuildingManager.getInstance().tic();
        Common.getInstance().tic();
    }

    @Override
    public void onUnitComplete(Unit unit) {
        System.out.println("unit complete unit.getType() = " + unit.getType());
        UnitManager.getInstance().addUnit(unit);
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("unit create unit.getType() = " + unit.getType());
        UnitManager.getInstance().onUnitCreate(unit);
    }

 @Override
    public void onUnitMorph(Unit unit) {
        System.out.println("unit morph unit.getType() = " + unit.getType());
        UnitManager.getInstance().onUnitCreate(unit);
    }


}
