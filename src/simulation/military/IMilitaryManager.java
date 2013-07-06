package simulation.military;

import java.util.Set;

import simulation.character.IGameCharacter;
import simulation.map.MapIndex;

/**
 * Interface for the military manager.
 */
public interface IMilitaryManager {

    /**
     * Enlist a dwarf into the military.
     * @param dwarf the dwarf to enlist
     */
    void enlistDwarf(IGameCharacter dwarf);

    /**
     * Get all the soldiers.
     * @return the soldiers
     */
    Set<IGameCharacter> getSoldiers();

    void militaryStation(MapIndex target);

    void cancelMilitaryStation();

    /**
     * Add a military manager listener.
     * @param listener the new listener
     */
    void addMilitaryManagerListener(IMilitaryManagerListener listener);

    /**
     * Remove a military manager listener.
     * @param listener the listener to remove
     */
    void removeMilitaryManagerListener(IMilitaryManagerListener listener);
}
