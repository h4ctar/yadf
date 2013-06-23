package simulation.job.jobstate;

/**
 * Interface for a job state.
 */
public interface IJobState {

    /**
     * Initialize the state.
     */
    void transitionInto();

    /**
     * Clean up the state.
     */
    void transitionOutOf();

    /**
     * Get the next state.
     * @return the next job state
     */
    IJobState getNextState();

    void interrupt(String message);
}
