package botmastr;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class APQInsertable {

    protected Long insertOrder = 0L;

    public APQInsertable() {
    }

    public APQInsertable(APQInsertable other) {
        this.insertOrder = other.insertOrder;
    }

    public Long getInsertOrder() {
        return this.insertOrder;
    }

    public void setInsertOrder(Long insertOrder) {
        this.insertOrder = insertOrder;
    }
}
