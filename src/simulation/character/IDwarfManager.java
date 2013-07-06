package simulation.character;

import java.util.Set;

import simulation.IRegion;
import simulation.farm.IGameObjectManager;
import simulation.labor.LaborType;
import simulation.map.MapIndex;

/**
 * Interface for the dwarf manager.
 */
public interface IDwarfManager extends IGameObjectManager {

    /**
     * Adds a new dwarf.
     * @param position the position of the new dwarf
     * @param region the region the dwarf is in
     */
    void addNewDwarf(MapIndex position, IRegion region);

    /**
     * Get a dwarf with a specific ID.
     * @param id the dwarfs ID
     * @return the dwarf
     */
    IGameCharacter getDwarf(int id);

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the labor that the dwarf needs to have, null if don't care
     * @return the dwarf
     */
    IGameCharacter getIdleDwarf(LaborType requiredLabor);

    /**
     * Gets a dwarf at a specific map index.
     * @param mapIndex the map index
     * @return the dwarf
     */
    IGameCharacter getDwarf(MapIndex mapIndex);

    /**
     * Gets a dwarf within a radius of a position.
     * @param position the position
     * @param radius the radius
     * @return the found dwarf, null if no dwarf within radius
     */
    IGameCharacter getDwarf(MapIndex position, int radius);

    /**
     * Gets all the dwarfs.
     * @return the dwarfs
     */
    Set<IGameCharacter> getDwarfs();

    /**
     * Add a listener that will be notified when a dwarf becomes free.
     * @param listener the listener to add
     */
    void addListener(ICharacterAvailableListener listener);

    /**
     * Remove a listener from the dwarf manager that was being notified of when a dwarf became free.
     * @param listener the listener to remove
     */
    void removeListener(ICharacterAvailableListener listener);

    /**
     * Update all the dwarfs.
     */
    void update();
}
