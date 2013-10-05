package yadf.simulation.character.component;

/**
 * Interface for an eating and drinking component.
 */
public interface IEatDrinkComponent extends ICharacterComponent {

    /**
     * Eat.
     */
    void eat();

    /**
     * Drink.
     */
    void drink();

    /**
     * Gets how hungry the character is as a percentage; 100% means they need to eat.
     * @return the hunger level
     */
    int getHunger();

    /**
     * Gets how thirsty the character is as a percentage; 100% means they need to drink.
     * @return the thirst level
     */
    int getThirst();

    /**
     * Turn on or off the spawning of eat and drink jobs.
     * @param spawnJobs true to spawn jobs
     */
    void setSpawnJobs(boolean spawnJobs);
}
