package botmastr.unit.objective;

import botmastr.unit.ITeamable;
import botmastr.unit.UnitData;

import java.util.List;
import java.util.Set;

/**
 * Base class for squad objectives.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class ASquadObjective {

    /**
     * Periodically ran by SquadManager to execute necessary checks and actions.
     * @param members squad members that should execute this objective
     */
    public abstract void tic(Set<UnitData> members);
}
