package botmastr.unit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import botmastr.common.Common;
import botmastr.navigation.NavigationManager;
import botmastr.unit.objective.AUnitObjective;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.WalkPosition;

/**
 * Represents a single BWAPI unit but adds aditional info.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitData implements ITeamable {
    /**
     * Must be larger than 0.
     */
    public static final int TRAIL_MAX_LENGTH = 20;
    public static final int MIN_FRAMES_BETWEEN_COMMANDS = 2;


    /**
     * BWAPI unit tied to this object.
     */
    protected Unit unit;

    /**
     * Plan containing current objectives of this unit.
     */
    protected PriorityQueue<AUnitObjective> plan = new PriorityQueue<>();


    /**
     * Last positions of the unit.
     */
    protected ArrayDeque<WalkPosition> trail = new ArrayDeque<>();

    public UnitData(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public PriorityQueue<AUnitObjective> getPlan() {
        return this.plan;
    }


    public void tic () {
        final AUnitObjective activeObjective = this.plan.peek();

        if (activeObjective != null) {
            activeObjective.tic();
        }
        else {
            //if no objective, then compute move
            if (!this.unit.getType().isWorker() && !onCommandCooldown()) {
                //use abilities? (and return)
                //attack if possible (and return)
                if (!CombatManager.getInstance().attack(this)) {
                    //move (and return)
                    NavigationManager.getInstance().computeMove(this);
                }
            }
        }
    }


    protected boolean onCommandCooldown() {
        return (Common.getInstance().getGame().getFrameCount() - this.getUnit().getLastCommandFrame()) < MIN_FRAMES_BETWEEN_COMMANDS;
    }

    /**
     * Adds a position into units trail.
     * @param position
     */
    public void addTrailPosition(WalkPosition position) {
        if (!this.trail.isEmpty()) {
            if (this.trail.getLast().equals(position))
                this.trail.add(position);
        }

        if (this.trail.size() > TRAIL_MAX_LENGTH) {
            this.trail.removeFirst();
        }
    }

    public ArrayDeque<WalkPosition> getTrail() {
        return this.trail;
    }

    /**
     * Adds an objective to this units plan.
     * @param newObjective New objective.
     */
    public void addObjective(AUnitObjective newObjective) {
        final AUnitObjective oldObjective = this.plan.peek();
        this.plan.add(newObjective);
        final AUnitObjective newTopObj = this.plan.peek();

        if (oldObjective == null || !oldObjective.equals(this.plan.peek())) {
            newObjective.execute();
        }
    }


    /**
     * Notifies unit that it's objective has finished.
     */
    public void objectiveFinished(AUnitObjective objective) {
        this.plan.remove(objective);

        if (this.plan.peek() != null) {
            this.plan.peek().execute();
        }
    }

    /**
     * Print debugging info for the unit.
     */
    public void debug() {
        int y = this.unit.getY();
        for (AUnitObjective o: this.plan) {
            o.debug(new Position(this.unit.getX(), y));
            y += 10;
        }

        if (this.unit.canTrain()) {
            Common.getInstance().getGame().drawTextMap(this.getUnit().getPosition().getX(), y, Integer.toString(this.unit.getTrainingQueue().size()));
        }
    }

    /**
     * Checks if this unit is doing something.
     * @return True if unit has no objectives, false otherwise.
     */
    public boolean idle() {
        return this.plan.size() == 0;
    }

    /**
     * Reacts to the information that the {@code Unit} represented by this {@code UnitData} has been destroyed.
     */
    public void destroy() {
        this.plan.stream().forEach(AUnitObjective::unitDestroyed);
    }

    @Override
    public String toString() {
        return this.unit.getType().toString() + "-" + this.unit.getID();
    }
}
