package botmastr;

import java.util.List;

/**
 * Manages owned bases.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class BaseManager {
    /**
     * Singleton instance.
     */
    private static final BaseManager INSTANCE = new BaseManager();

    /**
     * List of owned bases.
     */
    private List<MyBase> bases;

    /**
     * Private constructor because this is a singleton.
     */
    private BaseManager() {
    }

    public static BaseManager getInstance() {
        return INSTANCE;
    }
}
