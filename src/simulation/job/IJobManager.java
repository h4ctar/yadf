package simulation.job;

import java.util.Collection;
import java.util.List;

import simulation.IPlayerComponent;
import simulation.job.designation.AbstractDesignation;
import simulation.job.designation.DesignationType;

/**
 * Interface for a job manager.
 */
public interface IJobManager extends IPlayerComponent {

    /**
     * Add a new job.
     * @param job the job to add
     */
    void addJob(IJob job);

    /**
     * Gets a reference to all the designations.
     * @return A reference to the designations array
     */
    Collection<AbstractDesignation> getDesignations();

    /**
     * Gets a reference to a particular designation.
     * @param designateType the designation type that the caller wants
     * @return a reference to the designation
     */
    AbstractDesignation getDesignation(DesignationType designateType);

    /**
     * Add a new listener to be notified when jobs change.
     * @param listener the listener to add.
     */
    void addListener(IJobManagerListener listener);

    /**
     * Gets all the jobs.
     * @return the jobs
     */
    List<IJob> getJobs();

    /**
     * Get the job with ID.
     * @param jobId the job ID
     * @return the job
     */
    IJob getJob(int jobId);
}
