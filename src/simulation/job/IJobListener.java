package simulation.job;

/**
 * Interface for a job listener.
 */
public interface IJobListener {

    /**
     * The job has changed.
     * @param job the job that changed
     */
    void jobChanged(final IJob job);
}
