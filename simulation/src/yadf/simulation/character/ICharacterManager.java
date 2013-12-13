package yadf.simulation.character;

import java.util.List;

import yadf.simulation.IGameObjectManager;
import yadf.simulation.IPlayerComponent;
import yadf.simulation.labor.LaborType;
import yadf.simulation.map.MapIndex;

/**
 * Interface for the dwarf manager.
 */
public interface ICharacterManager extends IGameObjectManager<IGameCharacter>, IPlayerComponent {

    /**
     * Get a character with a specific ID.
     * @param id the characters ID
     * @return the character
     */
    IGameCharacter getCharacter(int id);

    /**
     * Gets an idle character.
     * @param requiredLabor the labor that the character needs to have, null if don't care
     * @return the character
     */
    IGameCharacter getIdleCharacter(LaborType requiredLabor);

    /**
     * Gets a character at a specific map index.
     * @param mapIndex the map index
     * @return the character
     */
    IGameCharacter getCharacter(MapIndex mapIndex);

    /**
     * Gets a character within a radius of a position.
     * @param position the position
     * @param radius the radius
     * @return the found character, null if no character within radius
     */
    IGameCharacter getCharacter(MapIndex position, int radius);

    /**
     * Gets all the characters.
     * @return the characters
     */
    List<IGameCharacter> getCharacters();

    /**
     * Add a listener that will be notified when a character becomes free.
     * @param listener the listener to add
     */
    void addListener(ICharacterAvailableListener listener);

    /**
     * Remove a listener from the character manager that was being notified of when a character became free.
     * @param listener the listener to remove
     */
    void removeListener(ICharacterAvailableListener listener);

    /**
     * Update all the characters.
     */
    void update();
}
