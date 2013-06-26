package simulation.character;

/**
 * Interface for a listener of a character.
 */
public interface ICharacterListener {

    /**
     * The character has changed in some way.
     * @param character the character that changed
     */
    void characterChanged(IGameCharacter character);
}
