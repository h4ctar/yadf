package yadf.simulation.job;

/**
 * Interface for a listener to the job manager.
 */
public interface IJobManagerListener {

    /**
     * A job have been added.
     * @param job the job that was added
     * @param index the index of the new job
     */
    void jobAdded(IJob job, int index);

    /**
     * A job has been removed.
     * @param job the job that was removed
     * @param index the index of the removed job
     */
    void jobRemoved(IJob job, int index);
}
