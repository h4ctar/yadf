package simulation;

import simulation.character.Dwarf;

/**
 * Interface for object that want to listen to a player.
 */
public interface IDwarfManagerListener {

    /**
     * A new dwarf has been added to the dwarf manager.
     * @param dwarf the new dwarf
     */
    void dwarfAdded(Dwarf dwarf);

    /**
     * A dwarf has been removed from the dwarf manager.
     * @param dwarf the removed dwarf
     */
    void dwarfRemoved(Dwarf dwarf);

    /**
     * A dwarf has become idle.
     * @param dwarf the idle dwarf
     */
    void dwarfNowIdle(Dwarf dwarf);
}
