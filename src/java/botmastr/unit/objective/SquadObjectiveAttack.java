package botmastr.unit.objective;

import botmastr.unit.UnitData;
import bwapi.Position;
import bwapi.Unit;

import java.util.Set;

/**
 *
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class SquadObjectiveAttack extends ASquadObjective {

    /**
     * Target that should be attacked.
     */
    protected Unit target;

    @Override
    public void tic(Set<UnitData> members) {
        members.stream().forEach(m -> m.getUnit().attack(this.target));
    }
}
