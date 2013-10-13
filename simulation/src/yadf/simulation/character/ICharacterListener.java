package yadf.simulation.character;

/**
 * Interface for a listener of a character.
 */
// TODO: delete this class
public interface ICharacterListener {

    /**
     * The character has changed in some way.
     * @param character the character that changed
     */
    void characterChanged(Object character);
}
