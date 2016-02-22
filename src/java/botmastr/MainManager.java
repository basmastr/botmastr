package botmastr;

/**
 * Makes top level decisions about what to do.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class MainManager extends AManager {
    /**
     * Singleton instance.
     */
    private static final MainManager INSTANCE = new MainManager();

    /**
     * Private because this is a singleton.
     */
    private MainManager() {}

    public static MainManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void tic() {

    }
}
