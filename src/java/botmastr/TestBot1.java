package java.botmastr;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

/**
 * Main class.
 * @author Tomas Tomek
 */
public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;

    private Boolean expanding = false;
    private Boolean building = false;
    private Unit expander = null;
    private BaseLocation expansion = null;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");

//        game.sendText("/cheats");
    }

    protected void drawExpansionDistances() {
        BaseLocation homeBaseLoc = BWTA.getNearestBaseLocation(self.getStartLocation());
        List<BaseLocation> baseLocations = BWTA.getBaseLocations();
        baseLocations = baseLocations.stream().filter(b -> b.getGroundDistance(homeBaseLoc) >= 0).collect(Collectors.toList());
        for(BaseLocation b : baseLocations) {
            game.drawTextMap(b.getX(), b.getY(), Double.toString(b.getGroundDistance(homeBaseLoc)));
        }
    }

    @Override
    public void onFrame() {
        game.setTextSize(10);
//        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        StringBuilder units = new StringBuilder("My units:\n");

        drawExpansionDistances();
        //iterate through my units
        List<Unit> allUnits = self.getUnits();
        long workerCount = allUnits.stream().filter(u -> u.getType() == UnitType.Protoss_Probe).count();
        long nexi = allUnits.stream().filter(u -> u.getType() == UnitType.Protoss_Nexus).count();
        for (Unit myUnit : allUnits) {
            if( myUnit.equals(expander) && building ){
                continue;
            }
            game.drawTextMap(myUnit.getX(), myUnit.getY(),  myUnit.getPosition().toString());
            game.drawTextMap(myUnit.getX(), myUnit.getY()+20,  myUnit.getOrderTargetPosition().toString());
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");
            //if there's enough minerals, train an SCV
            if (!building && expanding && myUnit.equals(expander)){
                if(myUnit.isIdle()){
                    System.out.println("EXPANDER_IDLE!!!!");
                    if(self.minerals() >= 400) {
                        System.out.println("building");
                        myUnit.build(expansion.getTilePosition(), UnitType.Protoss_Nexus);
                        building = true;
                    }
                }

//                expander = null;
            }
            if ((workerCount < 6 || nexi > 1) && myUnit.getType() == UnitType.Protoss_Nexus && self.minerals() >= 50) {
                myUnit.train(UnitType.Protoss_Probe);
            }
//            if ( myUnit.getType().isWorker() &&  self.minerals() >= 350) {
            if (!expanding && myUnit.getType().isWorker() &&  self.minerals() >= 350) {
                BaseLocation homeBaseLoc = BWTA.getNearestBaseLocation(self.getStartLocation());
                List<BaseLocation> baseLocations = BWTA.getBaseLocations();
                baseLocations = baseLocations.stream().filter(b -> b.getGroundDistance(homeBaseLoc) >= 0).collect(Collectors.toList());
                Collections.sort(baseLocations, new Comparator<BaseLocation>() {
                    public int compare(BaseLocation a, BaseLocation b) {
                        return (int)(a.getGroundDistance(homeBaseLoc) - b.getGroundDistance(homeBaseLoc));
                    }
                });


                baseLocations.remove(0);//remove starting base
//                System.out.println("i want to remove: " + baseLocations.get(0).getTilePosition().toString());
//                System.out.println("start location: " + self.getStartLocation().toString());
//                System.out.println("start base location: " + homeBaseLoc.getTilePosition().toString());
                for(BaseLocation b : baseLocations) {
                    if(myUnit.hasPath(b.getPosition())) {
//                        System.out.println("found accessible");
                        game.pingMinimap(b.getPosition());
                        myUnit.move(b.getPosition());
                        expanding = true;
                        expansion = b;
                        expander = myUnit;
                        break;
                    }
                }

//                for(BaseLocation b : baseLocations) {
//                    System.out.println(b.getPosition().toString());
//                }
//                System.out.println("move to pixels: " + myUnit.getOrderTargetPosition().toString());
                game.pingMinimap(myUnit.getOrderTargetPosition());
//                .sort((a, b) -> a.getGroundDistance(homeBaseLoc) - b.getGroundDistance(homeBaseLoc));
            }
            //if it's a drone and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                if(myUnit.equals(expander)) {
                    continue;
                }
                    Unit closestMineral = null;

                    //find the closest mineral
                    for (Unit neutralUnit : game.neutral().getUnits()) {
                        if (neutralUnit.getType().isMineralField()) {
                            if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                                closestMineral = neutralUnit;
                            }
                        }
                    }

                    //if a mineral patch was found, send the drone to gather it
                    if (closestMineral != null) {
                        myUnit.gather(closestMineral, false);
                    }
                }
        }

        //draw my units on screen
//        game.drawTextScreen(10, 25, units.toString());
    }

    public static void main(String[] args) {
        new TestBot1().run();
    }
}
