package simulation;

import simulation.character.Dwarf;

/**
 * Interface for object that want to listen to a player.
 */
public interface IDwarfManagerListener {

    /**
     * A new dwarf has been added to the dwarf manager.
     * @param newDwarf the new dwarf
     */
    void dwarfAdded(Dwarf newDwarf);

    /**
     * A dwarf has been removed from the dwarf manager.
     * @param removedDwarf the removed dwarf
     */
    void dwarfRemoved(Dwarf removedDwarf);

    /**
     * A dwarf has become idle.
     * @param idleDwarf the idle dwarf
     */
    void dwarfNowIdle(Dwarf idleDwarf);
}
