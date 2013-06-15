package simulation.character;

import java.util.Set;

import simulation.IDwarfManagerListener;
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
    Dwarf getDwarf(int id);

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the labor that the dwarf needs to have, null if don't care
     * @return the dwarf
     */
    Dwarf getIdleDwarf(LaborType requiredLabor);

    /**
     * Gets a dwarf at a specific map index.
     * @param mapIndex the map index
     * @return the dwarf
     */
    Dwarf getDwarf(MapIndex mapIndex);

    /**
     * Gets all the dwarfs.
     * @return the dwarfs
     */
    Set<Dwarf> getDwarfs();

    /**
     * Add a listener that will be notified when a dwarf is added or removed from the dwarf manager.
     * @param listener the listener to add
     */
    void addListener(IDwarfManagerListener listener);
}
