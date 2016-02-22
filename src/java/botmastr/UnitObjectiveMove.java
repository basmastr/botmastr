package botmastr;

import bwapi.Color;
import bwapi.Position;
import bwapi.UnitType;

/**
 * Objective for unit to move to a tile.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class UnitObjectiveMove extends AUnitObjective {

    public static final Integer DISTANCE_THRESHOLD = 150;

    /**
     * Where to move.
     */
    private Position position;

    /**
     *
     * @param unit
     * @param position
     */
    public UnitObjectiveMove(UnitData unit, Position position) {
        super(unit);
        this.position = position;
    }

    /**
     * @param unit
     * @param position
     * @param priority
     */
    public UnitObjectiveMove(UnitData unit, Position position, EPriority priority) {
        super(priority, unit);
        this.position = position;
    }

    /**
     * Execute the behaviour needed for completion of this objective.
     */
    @Override
    public void execute() {
        start();
    }


    public void tic() {
        this.unit.getUnit().move(this.position);
        if (this.unit.getUnit().getDistance(this.position) < DISTANCE_THRESHOLD) {
            this.unit.getUnit().stop();
            finish();
        }
        debug();
    }


    public void debug() {
//        Common.getInstance().getGame().drawCircleMap(this.position, 10, Color.Blue);
//        Common.getInstance().getGame().drawBoxMap(this.position, new Position(this.position.getX()+32, this.position.getY()+32), Color.Blue);
        Common.getInstance().getGame().drawLineMap(this.unit.getUnit().getPosition(), this.position, Color.White);
    }


    @Override
    public String getName() {
        return "MoveObjective";
    }
}
