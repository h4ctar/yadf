package yadf.simulation.character;

import yadf.simulation.IGameObjectManager;
import yadf.simulation.IPlayerComponent;
import yadf.simulation.labor.LaborType;
import yadf.simulation.map.MapIndex;

/**
 * Interface for the dwarf manager.
 */
public interface ICharacterManager extends IGameObjectManager<IGameCharacter>, IPlayerComponent {

    /**
     * Gets an idle character.
     * @param requiredLabor the labor that the character needs to have, null if don't care
     * @return the character
     */
    IGameCharacter getIdleCharacter(LaborType requiredLabor);

    /**
     * Gets a character within a radius of a position.
     * @param position the position
     * @param radius the radius
     * @return the found character, null if no character within radius
     */
    IGameCharacter getCharacter(MapIndex position, int radius);

    /**
     * Update all the characters.
     */
    void update();
}
