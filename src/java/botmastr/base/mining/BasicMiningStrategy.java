package botmastr.base.mining;

import botmastr.common.Common;
import botmastr.common.EPriority;
import botmastr.unit.UnitData;
import botmastr.unit.objective.UnitObjectiveMineGas;
import botmastr.unit.objective.UnitObjectiveMineMinerals;
import bwapi.Unit;

import java.util.Set;

/**
 * Basic strategy for gathering reources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class BasicMiningStrategy implements IMiningStrategy {

    public static final Integer MAX_WORKERS_PER_GAS = 3;
    // TODO: 24.2.2016 study effective mineral mining
    public static final Integer MAX_WORKERS_PER_MINERAL = 2;

    /**
     * todo temporary
     */
    public static final double GAS_MINERALS_RATIO = 0.15;

    @Override
    public void tic(Set<UnitData> workers, Set<Unit> minerals, Set<Unit> refineries) {
        // TODO: 24.2.2016 ask MainManager what priority gas gathering has, maybe in form of minerals:gas, e.g. 0.8
        final int maxGasMinersNeeded = refineries.size() * MAX_WORKERS_PER_GAS;
        int gasMinerCountNeeded = (int) Math.ceil(workers.size() * GAS_MINERALS_RATIO);
        int currentGasMiners = refineries.stream().mapToInt(o -> minerCountOnTarget(workers, o)).sum();

        if (gasMinerCountNeeded > maxGasMinersNeeded) {
            gasMinerCountNeeded = maxGasMinersNeeded;
        }

        final int updateInterval = 100;

        for (UnitData worker :
                workers) {
            if (Common.getInstance().getGame().getFrameCount() % updateInterval == 0) {
                if (worker.getPlan().size() == 1 && worker.getPlan().peek() instanceof UnitObjectiveMineMinerals) {
                    worker.getPlan().peek().finish();
                }
            }
            if (worker.idle()) {
                if (currentGasMiners < gasMinerCountNeeded) {
                    for (Unit gas :
                            refineries) {
                        if (minerCountOnTarget(workers, gas) < MAX_WORKERS_PER_GAS) {
                            worker.addObjective(new UnitObjectiveMineGas(worker, gas, EPriority.MEDIUM));
                            currentGasMiners++;
                            break;
                        }
                    }
                }
                else {
                    //add worker to mineralPatch
                    for (Unit mineralPatch :
                            minerals) {
                        if (minerCountOnTarget(workers, mineralPatch) < MAX_WORKERS_PER_MINERAL) {
                            worker.addObjective(new UnitObjectiveMineMinerals(worker, mineralPatch, EPriority.MEDIUM));
                            break;
                        }
                    }
                }
            }
        }
    }


    protected int minerCountOnTarget(Set<UnitData> workers, Unit target) {
        if (target.getType().isMineralField()) {
            return (int) workers.stream().filter(u -> u.getPlan().stream().filter(UnitObjectiveMineMinerals.class::isInstance)
                    .map(UnitObjectiveMineMinerals.class::cast).anyMatch(o -> o.getMineral().equals(target))).count();
        }
        //target is gasRefinery
        else {
            return (int) workers.stream().filter(u -> u.getPlan().stream().filter(UnitObjectiveMineGas.class::isInstance)
                    .map(UnitObjectiveMineGas.class::cast).anyMatch(o -> o.getRefinery().equals(target))).count();
        }
    }
}
