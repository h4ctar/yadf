package simulation.job;

/**
 * Interface for a listener to the job manager.
 */
public interface IJobManagerListener {

    /**
     * A job have been added.
     * @param job the job that was added
     */
    void jobsAdded(IJob job);

    /**
     * A job has been removed.
     * @param job the job that was removed
     */
    void jobRemoved(IJob job);
}
