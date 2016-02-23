package botmastr;

/**
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public abstract class APQInsertable {

    protected Long insertOrder = null;

    public APQInsertable() {
    }

    public APQInsertable(APQInsertable other) {
        this.insertOrder = other.insertOrder;
    }


    public boolean isInserted() {
        return this.insertOrder != null;
    }

    public Long getInsertOrder() {
        return this.insertOrder;
    }

    public void setInsertOrder(Long insertOrder) {
        this.insertOrder = insertOrder;
    }
}
