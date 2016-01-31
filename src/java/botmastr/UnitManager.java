package botmastr;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import bwapi.*;

/**
 * General unit manager.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class UnitManager extends AManager implements IManager {
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
    public void addUnit(Unit unit) {
        // TODO: 14.1.2016 copy from loki (what to do on various owners)
        final UnitData unitData = new UnitData(unit);
        this.units.put(unit.getID(), unitData);

        if (isMainBuilding(unitData.getUnit()) && isMine(unitData.getUnit())) {
            new MyBase(unitData);
        }
        else if (unit.getType() == UnitType.Resource_Vespene_Geyser){
            BaseManager.getInstance().addGeyser(unit);
        }

        else if (unit.getType().isMineralField()){
            BaseManager.getInstance().addMineralPatch(unit);
        }
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

//    /**
//     * Gets the list of owned military units.
//     * @return List of owned military units.
//     */
//    public List<UnitData> getMilitaryUnits() {
//        // TODO: 3.1.2016
//        return this.units;
//    }

    /**
     * Checks if supplied unit is command center, nexus or hatchery.
     * @param unit Unit to check.
     * @return TRUE if unit is of type command center, nexus or hatchery.
     */
    private boolean isMainBuilding(Unit unit) {
        return unit.getType() == UnitType.Terran_Command_Center || unit.getType() == UnitType.Zerg_Hatchery || unit.getType() == UnitType.Protoss_Nexus;
    }

    @Override
    public void tic() {
//        checkUnassignedUnits();
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
}
