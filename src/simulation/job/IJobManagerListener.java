package simulation.job;

/**
 * Interface for a listener to the job manager.
 */
public interface IJobManagerListener {

    /**
     * A job has been removed.
     * 
     * @param index The index of the removed job
     */
    void jobRemoved(int index);

    /**
     * Jobs have been added.
     * 
     * @param firstIndex the index of the first job
     * @param lastIndex the index of the last job
     */
    void jobsAdded(int firstIndex, int lastIndex);
}
