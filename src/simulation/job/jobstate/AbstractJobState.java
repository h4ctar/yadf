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
}
