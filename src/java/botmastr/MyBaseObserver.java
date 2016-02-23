package botmastr;

import botmastr.base.MyBase;

/**
 * Interface for every object interested in reacting to MyBase events.
 * @see MyBase
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public interface MyBaseObserver {
    /**
     * Event called after creation of MyBase object.
     * @param base MyBase that was just created.
     */
    void onMyBaseCreated(MyBase base);
}
