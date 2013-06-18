package simulation.character;

/**
 * Interface for a character listener.
 */
public interface ICharacterListener {

    /**
     * Method that will be called whenever the character changes.
     * @param character the character that has changed
     */
    void characterChanged(GameCharacter character);
}
