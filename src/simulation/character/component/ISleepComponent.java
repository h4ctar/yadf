package simulation.character.component;

/**
 * Interface for a sleep component.
 */
public interface ISleepComponent extends ICharacterComponent {

    /**
     * Sleep.
     */
    void sleep();

    /**
     * Turn on or off the spawning of sleep jobs.
     * @param spawnJobs true to spawn jobs
     */
    void setSpawnJobs(boolean spawnJobs);
}
