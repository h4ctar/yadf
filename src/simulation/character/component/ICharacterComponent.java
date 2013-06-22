package simulation.character.component;

import simulation.Region;

/**
 * Interface that all character components must implement.
 */
public interface ICharacterComponent {

    /**
     * Update the component.
     * @param region the region
     */
    void update(Region region);

    /**
     * Add a listener to the component that will be notified whenever it changes.
     * @param listener the listener to add
     */
    void addListener(ICharacterComponentListener listener);

    /**
     * Remove a listener from this component.
     * @param listener the listener to remove
     */
    void removeListener(ICharacterComponentListener listener);

    /**
     * Kill.
     */
    void kill();
}
