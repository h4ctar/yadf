package simulation;

import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * Interface of a player.
 */
public interface IPlayer extends IGameObject {

    /**
     * Set the character component.
     * @param componentInterface the component interface to set
     * @param component the new component to apply
     * @param <T> the component interface
     */
    <T extends IPlayerComponent> void setComponent(final Class<T> componentInterface, final T component);

    /**
     * Get a character component.
     * @param componentInterface the component interface to get
     * @return the character component
     * @param <T> the component interface
     */
    <T extends IPlayerComponent> T getComponent(final Class<T> componentInterface);

    /**
     * Remove a character component.
     * @param componentInterface the component to remove.
     * @param <T> the component interface
     */
    <T extends IPlayerComponent> void removeComponent(final Class<T> componentInterface);

    /**
     * Update.
     */
    void update();

    /**
     * Get the players name.
     * @return the players name
     */
    String getName();

    boolean checkAreaValid(MapArea area);

    boolean checkAreaValid(MapIndex mapIndex);
}
