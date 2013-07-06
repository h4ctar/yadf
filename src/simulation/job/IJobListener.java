package simulation.job;

/**
 * Interface for a job listener.
 */
public interface IJobListener {

    /**
     * The job has finished.
     * @param job the job that changed
     */
    void jobDone(final IJob job);

    /**
     * The job changed state.
     * @param job the job that changed
     */
    void jobChanged(final IJob job);
}
