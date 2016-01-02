import bwapi.Unit;

/**
 * Created by Tomas Tomek on 21.12.2015.
 * tomas.tomek333@gmail.com
 */
public class UnitData {
    protected Unit unit;
    protected byte state;
    public static final byte STATE_AVAILABLE = 0;
    public static final byte STATE_BUSY = 1;

    public UnitData(Unit unit, byte state) {
        this.unit = unit;
        this.state = state;
    }

    public UnitData(Unit unit) {
        this.unit = unit;
        this.state = STATE_AVAILABLE;
    }

    public Unit getUnit() {
        return unit;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}
