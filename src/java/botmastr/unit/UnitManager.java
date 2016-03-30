package botmastr.unit;

import java.util.*;
import java.util.stream.Collectors;

import botmastr.common.AManager;
import botmastr.common.EPriority;
import botmastr.production.training.TrainingManager;
import botmastr.unit.objective.AUnitObjective;
import botmastr.base.MyBase;
import botmastr.unit.objective.UnitObjectiveBuild;
import botmastr.unit.objective.UnitObjectiveScout;
import bwapi.*;
import javafx.geometry.Pos;

/**
 * General unit manager.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class UnitManager extends AManager {
    /**
     * Singleton intance.
     */
    private static final UnitManager INSTANCE = new UnitManager();

    /**
     * Unit list
     */
    private Map<Integer, UnitData> units = new HashMap<>();

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
    }

    /**
     * Adds a unit to the list.
     * @param unit
     */
    public UnitData addUnit(Unit unit) {
        // TODO: 14.1.2016 copy from loki (what to do on various owners)
        final UnitData unitData = new UnitData(unit);
        this.units.put(unit.getID(), unitData);

        if (isMainBuilding(unitData.getUnit()) && isMine(unitData.getUnit())) {
            new MyBase(unitData);
        }
//        else if (unit.getType() == UnitType.Resource_Vespene_Geyser){
//            BaseManager.getInstance().addGeyser(unit);
//        }
//
//        else if (unit.getType().isMineralField()){
//            BaseManager.getInstance().addMineralPatch(unit);
//        }

        return unitData;
    }

    /**
     *
     * @return
     */
    public List<UnitData> getUnitsByType(UnitType type) {
        return this.units.values().stream().filter(u -> u.getUnit().getType().equals(type)).collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public List<UnitData> getUnitsByType(List<UnitType> types) {
        return this.units.values().stream().filter(u -> types.contains(u.getUnit().getType())).collect(Collectors.toList());
    }

    /**
     *
     * @param middle
     * @param radius
     * @param type
     * @return
     */
    public List<Unit> getUnitsInRadius(Position middle, Integer radius, UnitType type) {
        return this.bwapi.getGame().getUnitsInRadius(middle, radius).stream().filter(u -> u.getType() == type).collect(Collectors.toList());
    }

    /**
     *
     * @param middle
     * @param radius
     * @param type
     * @return
     */
    public List<Unit> getUnitsInRadius(Position middle, Integer radius, List<UnitType> types) {
        return bwapi.getGame().getUnitsInRadius(middle, radius).stream().filter(u -> types.contains(u.getType())).collect(Collectors.toList());
    }

    /**
     * Checks if supplied unit is owned by the bot.
     * @param unit Unit which's ownership to check.
     * @return True if unit is owned by the bot, false otherwise.
     */
    private boolean isMine(Unit unit) {
        return unit.getPlayer().equals(this.bwapi.getGame().self());
    }

    /**
     * Checks if supplied unit is command center, nexus or hatchery.
     * @param unit Unit to check.
     * @return TRUE if unit is of type command center, nexus or hatchery.
     */
    private boolean isMainBuilding(Unit unit) {
        return unit.getType() == UnitType.Terran_Command_Center || unit.getType() == UnitType.Zerg_Hatchery || unit.getType() == UnitType.Protoss_Nexus;
    }

    @Override
    public void tic()  {
        this.units.values().forEach(UnitData::tic);
//        checkUnassignedUnits();
    }

    /**
     *
     * @param unit
     */
    public void onUnitCreate(Unit unit) {
        if (unit.getType().isBuilding() && isMine(unit)) {
            //todo this is stupid
            final List<UnitObjectiveBuild> buildObjectives = this.units.values().stream()
                    .map(u -> u.plan.peek())
                    .filter(UnitObjectiveBuild.class::isInstance)
                    .map(UnitObjectiveBuild.class::cast)
                    .filter(o -> o.getPosition().equals(unit.getTilePosition()) && o.getBuilding().equals(unit.getType()))
                    .collect(Collectors.toList());
            if (buildObjectives.size() > 0) {
                buildObjectives.get(0).finish();
            }
        }
    }

    /**
     * Reacts to building being completed.
     * @param unit building that was completed
     */
    protected void buildingCompleted(Unit unit) {
        if (isMine(unit)) {
            final UnitData unitData = UnitManager.getInstance().addUnit(unit);

            if (unit.canTrain()) {
                TrainingManager.getInstance().loadBalanceQueues();
            }
        }
    }

    /**
     * Reacts to unit being completed.
     * @param unit unit that has been completed
     */
    protected void unitCompleted(Unit unit) {
        if (isMine(unit)) {
            final UnitData unitData = UnitManager.getInstance().addUnit(unit);

            if (!unit.getType().isBuilding() && unit.canAttack() && !unit.getType().isWorker()) {
                SquadManager.getInstance().getSquad(unit).addMember(unitData);
            }
        }
    }



    /**
     * Let's {@code UnitManager} react to a unit being completed.
     * @param unit unit that has been completed
     */
    public void onUnitComplete(Unit unit) {
        if (unit.getType().isBuilding()) {
            this.buildingCompleted(unit);
        }
        else {
            this.unitCompleted(unit);
        }
    }

    /**
     * Let's {@code UnitManager} react to a unit being destroyed.
     * @param unit unit that has been destroyed
     */
    public void onUnitDestroy(Unit unit) {
        if (isMine(unit)) {
            final UnitData unitData = this.getUnitData(unit);

            if (unitData != null) {
                unitData.destroy();
                this.units.remove(unit.getID());
            }
        }
    }

    /**
     * Gets {@code UnitData} object representing this unit if this unit is managed by {@code UnitManager}.
     * @param unit unit to get
     * @return {@code UnitData} representing supplied unit, {@code null} otherwise
     */
    public UnitData getUnitData(Unit unit) {
        return this.units.get(unit.getID());
    }

    /**
     * Goes through all owned units and if it finds some that are not assigned to managers, it will assign them.
     */
    private void checkUnassignedUnits() {
        for (Unit unit : this.bwapi.getGame().getAllUnits()) {
            if (isMine(unit) && !this.units.containsKey(unit.getID())) {
//                System.out.println("unit.getType() = " + unit.getType().toString());
                addUnit(unit);
            }
        }
//        this.units.addAll(this.bwapi.getGame().getAllUnits().stream().map(UnitData::new).collect(Collectors.toMap()));

    }

    public Map<Integer, UnitData> getUnits() {
        return this.units;
    }


    /**
     * Find a suitable unit and send it to scout supplied position.
     * @param position position to be scouted
     * @return {@code UnitData} representing the unit doing the scouting
     */
    public UnitData orderScouting(Position position) {
        final UnitData scout = this.units.values().stream().filter(u -> u.getUnit().canMove() && u.getPlan().size() <= 1).sorted((u1, u2) -> u1.getUnit().getDistance(position) - u2.getUnit().getDistance(position)).findFirst().get();

        if (scout != null) {
            scout.addObjective(new UnitObjectiveScout(scout, position, EPriority.HIGH));
        }

        return scout;
    }
}
