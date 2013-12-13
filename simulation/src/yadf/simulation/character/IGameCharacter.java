package yadf.simulation.character;

import yadf.simulation.IEntity;
import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.component.ICharacterComponent;
import yadf.simulation.job.IJob;

/**
 * Interface for a game character.
 */
public interface IGameCharacter extends IEntity {

    /**
     * Set the character component.
     * @param componentInterface the component interface to set
     * @param component the new component to apply
     * @param <T> the component interface
     */
    <T extends ICharacterComponent> void setComponent(final Class<T> componentInterface, final T component);

    /**
     * Get a character component.
     * @param componentInterface the component interface to get
     * @return the character component
     * @param <T> the component interface
     */
    <T extends ICharacterComponent> T getComponent(final Class<T> componentInterface);

    /**
     * Remove a character component.
     * @param componentInterface the component to remove.
     * @param <T> the component interface
     */
    <T extends ICharacterComponent> void removeComponent(final Class<T> componentInterface);

    /**
     * Kill the game character.
     */
    void kill();

    /**
     * Is the character dead.
     * @return true if they're dead
     */
    boolean isDead();

    /**
     * Get the player that the character belongs to.
     * @return the player
     */
    IPlayer getPlayer();

    /**
     * Add a listener to this character that will be notified when the character becomes available.
     * @param listener the listener to add
     */
    void addListener(ICharacterAvailableListener listener);

    /**
     * Remove a listener from this character that was being notified of the characters availablilty.
     * @param listener the listener to remove
     */
    void removeListener(ICharacterAvailableListener listener);

    /**
     * Add a listener to this character that will be notified when the character changes.
     * @param listener the listener to add
     */
    void addListener(ICharacterListener listener);

    /**
     * Remove a listener from this character that was being notified of changes.
     * @param listener the listener to remove
     */
    void removeListener(ICharacterListener listener);

    /**
     * Set the job of the character.
     * @param job the new job
     */
    void setJob(IJob job);

    /**
     * Update the game character.
     */
    void update();

    /**
     * Get the name of the character.
     * @return the name
     */
    String getName();

    /**
     * Get the region that this game character is currently in.
     * @return the region
     */
    IRegion getRegion();
}
