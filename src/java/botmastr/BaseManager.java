package java.botmastr;

/**
 * Manages basebuilding.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BaseManager extends ABaseManager {
    /**
     * Singleton instance.
     */
    public static final BaseManager INSTANCE = new BaseManager();

    /**
     * Private because this is a singleton.
     */
    private BaseManager() {
    }

    public static BaseManager getInstance() {
        return INSTANCE;
    }
}
