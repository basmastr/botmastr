package botmastr;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class Cost {
    protected Integer minerals;
    protected Integer gas;

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
