package simulation.character;

/**
 * Interface for anyone to implement who wants to listen for a character becoming free.
 */
public interface ICharacterAvailableListener {

    /**
     * A character has become available.
     * @param character the available character
     */
    void characterAvailable(IGameCharacter character);
}
