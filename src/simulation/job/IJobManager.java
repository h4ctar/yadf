package simulation.job;

/**
 * Interface for a job manager.
 */
public interface IJobManager {

    /**
     * Add a new job.
     * @param job the job to add
     */
    void addJob(IJob job);
}
