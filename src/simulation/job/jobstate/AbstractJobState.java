package simulation.job.jobstate;

import simulation.job.AbstractJob;

/**
 * An abstract job state.
 */
public abstract class AbstractJobState implements IJobState {

    /** The job that this state belongs to. */
    private final AbstractJob job;

    /**
     * Constructor.
     * @param jobTmp the job that this state belongs to
     */
    public AbstractJobState(final AbstractJob jobTmp) {
        job = jobTmp;
    }

    /**
     * Gets the job that this state belongs to.
     * @return the job that this state belongs to
     */
    protected AbstractJob getJob() {
        return job;
    }

    /**
     * Do the final actions and tell the job that the state is done.
     */
    protected void finishState() {
        doFinalActions();
        job.stateDone(this);
    }

    /**
     * Called by the state when it's done.
     */
    protected void doFinalActions() {

    }
}
