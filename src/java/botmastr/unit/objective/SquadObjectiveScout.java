package botmastr.unit.objective;

import botmastr.unit.UnitData;
import bwapi.Position;

import java.util.Set;

/**
 * Objective for a squad to scout a location on the map.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class SquadObjectiveScout extends ASquadObjective {

    /**
     * Location to scout.
     */
    protected Position location;

    public SquadObjectiveScout(Position location) {
        this.location = location;
    }

    @Override
    public void tic(Set<UnitData> members) {
        members.stream().forEach(m -> m.getUnit().move(this.location));
    }
}
