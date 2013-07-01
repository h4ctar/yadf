package simulation.character.component;

/**
 * Interface for a character component listener.
 */
public interface ICharacterComponentListener {

    /**
     * Called when the component is changed.
     * @param component the component that changed
     */
    void componentChanged(Object component);
}
