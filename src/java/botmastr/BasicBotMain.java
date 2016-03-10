package botmastr;

import botmastr.base.BaseManager;
import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.production.building.BuildingManager;
import botmastr.production.building.BuildingQueueItem;
import botmastr.production.resources.ResourceManager;
import botmastr.production.training.TrainingManager;
import botmastr.production.training.TrainingQueueItem;
import botmastr.unit.SquadManager;
import botmastr.unit.UnitManager;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

import java.util.List;

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
        TrainingManager.getInstance().init(this.mirror);
        SquadManager.getInstance().init(this.mirror);
        this.buildOrder1();
        this.trainSome();
   }


    private void trainSome() {
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.HIGH, UnitType.Protoss_Probe));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.HIGH, UnitType.Protoss_Probe));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.HIGH, UnitType.Protoss_Probe));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.LOW, UnitType.Protoss_Zealot));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
//        TrainingManager.getInstance().addQueueItem(new TrainingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Probe));
    }

    private void buildOrder1() {
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Terran_Refinery));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Terran_Supply_Depot));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Terran_Barracks));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Gateway));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Assimilator));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Pylon));
        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Gateway));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Forge));
        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.MEDIUM, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Photon_Cannon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Pylon));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.HIGH, UnitType.Protoss_Nexus));
//        BuildingManager.getInstance().addQueueItem(new BuildingQueueItem(EPriority.LOW, UnitType.Protoss_Pylon));

    }

    @Override
    public void onFrame() {
        final long startTime = System.nanoTime();
        UnitManager.getInstance().tic();
        BaseManager.getInstance().tic();
        ResourceManager.getInstance().tic();
        BuildingManager.getInstance().tic();
        TrainingManager.getInstance().tic();
        SquadManager.getInstance().tic();
        Common.getInstance().tic();

        if (Common.getInstance().time()) {
            printOnFrameTimer(startTime);
        }
//            Common.getInstance().getGame().drawCircleMap(BWTA.getStartLocations().stream().filter(p -> !p.getTilePosition().equals(this.mirror.getGame().self().getStartLocation())).findFirst().get().getPosition(), 200, Color.Purple);

//        BaseLocation baseLoc = BWTA.getStartLocation(enemy);
//        Position pos = BWTA.getStartLocation(enemy).getPosition();
//        List<Player> enemes =  this.mirror.getGame().enemies();
//        Position enemyPos = enemy.getStartLocation().toPosition();
//        TilePosition loca = enemy.getStartLocation();
//        Common.getInstance().getGame().drawCircleMap(enemyPos, 200, Color.Purple);

    }

    @Override
    public void onUnitComplete(Unit unit) {
        System.out.println("unit complete unit.getType() = " + unit.getType());
        UnitManager.getInstance().onUnitComplete(unit);
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


    private void printOnFrameTimer(long startTime) {
        final long endTime = System.nanoTime();
        final long duration = (endTime - startTime) / 1000000;
        System.out.println(duration + "ms");
    }


}
