package botmastr.common;

/**
 * Class representing mineral and gas cost of a unit, a building or an upgrade.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class Cost {

    /**
     * Mineral cost.
     */
    protected Integer minerals;

    /**
     * Gas cost.
     */
    protected Integer gas;

    /**
     * Copy constructor.
     * @param other original
     */
    public Cost(Cost other) {
        this.minerals = other.minerals;
        this.gas = other.gas;
    }

    public Cost(Integer minerals, Integer gas) {
        this.minerals = minerals;
        this.gas = gas;
    }

    public Integer getGas() {
        return this.gas;
    }

    public Integer getMinerals() {
        return this.minerals;
    }
}
