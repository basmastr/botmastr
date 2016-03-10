package botmastr.unit;

import botmastr.unit.objective.ASquadObjective;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents group of units with common objectives.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class Squad {

    /**
     * Units that are members of the squad.
     */
    Set<UnitData> members = new HashSet<>();

    /**
     * Current objective of the squad.
     */
    ASquadObjective objective;


    public void tic() {
        if (this.objective != null) {
            this.objective.tic(this.members);
        }
    }


    /**
     * Adds a group of members to the squad.
     * @param newMembers list of new members
     */
    public void addMembers(Set<UnitData> newMembers) {
        this.members.addAll(newMembers);
    }

    /**
     * Adds a new member to the squad.
     * @param newMember member to be added
     */
    public void addMember(UnitData newMember) {
        this.members.add(newMember);
    }

    /**
     * If the supplied member is part of the squad, he is removed from it, otherwise nothing happens.
     * @param member member to be removed
     */
    public void removeMember(UnitData member) {
        this.members.remove(member);
    }

    public void setObjective(ASquadObjective objective) {
        this.objective = objective;
    }

    /**
     * Checks if the squad has an objective.
     * @return True if the squad has an objective, false otherwise.
     */
    public boolean hasObjective() {
        return this.objective != null;
    }
}
