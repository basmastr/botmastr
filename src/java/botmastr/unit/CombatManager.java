package botmastr.unit;

import botmastr.common.AManager;
import botmastr.common.Common;
import bwapi.Mirror;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the actions of squads.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class CombatManager extends AManager {
    /**
     * Singleton intance.
     */
    private static final CombatManager INSTANCE = new CombatManager();

    /**
     * Private constructor cos this is a singleton.
     */
    private CombatManager() {
    }

    public static CombatManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void init(Mirror bwapi) {
        super.init(bwapi);
    }

    @Override
    public void tic() {
    }


    public boolean attack(UnitData unit) {
        if (!canAttack(unit)) {
            return false;
        }

        final Unit target = findTarget(unit);
        if (target != null) {
            return unit.getUnit().attack(target);
        }
        else {
            //no target found
            // TODO: 6.4.2016 debug
//            System.out.println("No target found for " + unit.toString());
            return false;
        }
    }

    /**
     *
     * @param unit attacking unit which needs a target
     * @return Enemy unit to be attacked or {@code null} if no target was found.
     */
    public Unit findTarget(UnitData unit) {
        final Unit bwapiUnit = unit.getUnit();
//        final List<Unit> potTargetList = bwapiUnit.getUnitsInRadius(bwapiUnit.getType().groundWeapon().maxRange());
//        potTargetList.addAll(bwapiUnit.getUnitsInRadius(bwapiUnit.getType().airWeapon().maxRange()));
        return Common.getInstance().getEenemy().getUnits().stream()
                .filter(bwapiUnit::isInWeaponRange)
                .min((u1, u2) -> evaluateTarget(unit, u1) - evaluateTarget(unit, u2))
                .orElse(null);
    }

    /**
     * Assigns a value to a target. Lower value means {@code target} is a better unit to target.
     * @param unit unit that will be attacking
     * @param target potential target for {@code unit}
     * @return Value assigned to the target.
     */
    protected int evaluateTarget(UnitData unit, Unit target) {
        // TODO: 6.4.2016 find the best one to be attacked based on distance, health, score for killing the unit
        return target.getHitPoints();
    }

    /**
     * @param unit
     * @return
     */
    public boolean canAttack(UnitData unit) {
        return unit.getUnit().canAttack() && unit.getUnit().getGroundWeaponCooldown() == 0;// && unit.getUnit().isIdle();
    }
}
