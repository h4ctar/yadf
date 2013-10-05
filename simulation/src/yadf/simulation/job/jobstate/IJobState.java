package yadf.simulation.job.jobstate;

/**
 * Interface for a job state.
 */
public interface IJobState {

    /**
     * Initialize the state.
     */
    void start();

    /**
     * Interrupt the state.
     * @param message why the state was interrupted
     */
    void interrupt(String message);

    /**
     * Get the next state.
     * @return the next job state
     */
    IJobState getNextState();
}
