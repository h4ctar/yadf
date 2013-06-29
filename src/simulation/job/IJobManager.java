package simulation.job;

import java.util.Collection;

import simulation.job.designation.AbstractDesignation;

/**
 * Interface for a job manager.
 */
public interface IJobManager {

    /**
     * Add a new job.
     * @param job the job to add
     */
    void addJob(IJob job);

    Collection<AbstractDesignation> getDesignations();
}
