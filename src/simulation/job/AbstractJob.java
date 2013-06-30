package simulation.job;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import logger.Logger;
import simulation.IPlayer;
import simulation.job.jobstate.IJobState;

/**
 * An abstract job class that implements the listeners storage and notification method.
 */
public abstract class AbstractJob implements IJob {

    /** The listeners to this job. */
    private final List<IJobListener> listeners = new CopyOnWriteArrayList<>();

    /** The player that this job is for. */
    private final IPlayer player;

    /** The current job state. */
    private IJobState jobState;

    /** Is the job done. */
    private boolean done;

    /**
     * Constructor.
     * @param playerTmp the player that this job belongs to
     */
    AbstractJob(final IPlayer playerTmp) {
        Logger.getInstance().log(this, "Job created: " + toString());
        player = playerTmp;
    }

    @Override
    public String getStatus() {
        return jobState.toString();
    }

    /**
     * Set the state of the job.
     * @param jobStateTmp the job state
     */
    protected void setJobState(final IJobState jobStateTmp) {
        jobState = jobStateTmp;
        jobState.start();
    }

    /**
     * Get the player.
     * @return the player
     */
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * The current state is finished.
     * @param jobStateTmp the job state
     */
    public void stateDone(final IJobState jobStateTmp) {
        Logger.getInstance().log(this, "Job state done: " + jobState.toString());
        assert jobState == jobStateTmp;
        jobState = jobState.getNextState();
        if (jobState != null) {
            Logger.getInstance().log(this, "Transitioning: " + jobStateTmp.toString() + " -> " + jobState.toString());
            jobState.start();
        } else {
            Logger.getInstance().log(this, "Job done");
            done = true;
            notifyListeners();
        }
    }

    /**
     * The current state was interrupted.
     * @param jobStateTmp the job state
     * @param message why the state was interrupted
     */
    public void stateInterrupted(final IJobState jobStateTmp, final String message) {
        assert jobState == jobStateTmp;
        interrupt(message);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been interrupted: " + message, true);
        jobState.interrupt(toString() + " was interrupted");
    }

    @Override
    public final void addListener(final IJobListener listener) {
        listeners.add(listener);
    }

    @Override
    public final void removeListener(final IJobListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that this job has changed.
     */
    private void notifyListeners() {
        for (IJobListener listener : listeners) {
            listener.jobDone(this);
        }
    }
}
