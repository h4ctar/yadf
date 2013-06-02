package simulation.job;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract job class that implements the listeners storage and notification method.
 */
public abstract class AbstractJob implements IJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -2079523360053269856L;

    /** The listeners to this job. */
    private final List<IJobListener> listeners = new ArrayList<>();

    @Override
    public void addListener(final IJobListener listener) {
        listeners.add(listener);
    }

    /**
     * Notify all the listeners that this job has changed.
     */
    protected void notifyListeners() {
        for (IJobListener listener : listeners) {
            listener.jobChanged(this);
        }
    }
}
