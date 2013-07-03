package simulation.character;

import simulation.IEntity;
import simulation.IPlayer;
import simulation.IRegion;
import simulation.character.component.ICharacterComponent;

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
     * Is the character locked.
     * @return true if the character is locked
     */
    boolean isLocked();

    /**
     * Release the lock on a character.
     */
    void releaseLock();

    /**
     * Acquire a lock on a character.
     * @return true if the lock was acquired
     */
    boolean acquireLock();

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
