package simulation.character;

import java.util.Set;

import simulation.labor.LaborType;
import simulation.map.MapIndex;

/**
 * Interface for the dwarf manager.
 */
public interface IDwarfManager {

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
     * Gets all the dwarfs.
     * @return the dwarfs
     */
    Set<IGameCharacter> getDwarfs();

    /**
     * Add a listener that will be notified when a dwarf is added or removed from the dwarf manager.
     * @param listener the listener to add
     */
    void addListener(IDwarfManagerListener listener);

    /**
     * Remove a listener from the dwarf manager that was being notified of when a dwarf was added or removed from the
     * manager.
     * @param listener the listener to remove
     */
    void removeListener(IDwarfManagerListener listener);

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
}
