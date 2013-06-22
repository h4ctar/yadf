package simulation.character;

import simulation.IPlayer;
import simulation.character.component.ICharacterComponent;
import simulation.map.MapIndex;

public interface IGameCharacter {

    <T extends ICharacterComponent> void setComponent(final Class<T> componentInterface, final T component);

    <T extends ICharacterComponent> T getComponent(final Class<T> componentInterface);

    void kill();

    boolean isDead();

    MapIndex getPosition();

    void setPosition(MapIndex position);

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

    boolean isLocked();

    void releaseLock();

    boolean acquireLock();
}
