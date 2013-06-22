package simulation.job;

/**
 * Interface for a listener to the job manager.
 */
public interface IJobManagerListener {

    /**
     * A job has been removed.
     * @param job the job that was removed
     */
    void jobRemoved(IJob job);

    /**
     * Jobs have been added.
     * 
     * @param firstIndex the index of the first job
     * @param lastIndex the index of the last job
     */
    void jobsAdded(int firstIndex, int lastIndex);
}
