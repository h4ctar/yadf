package simulation.character;

/**
 * Interface for object that want to listen to the dwarf manager.
 */
public interface IDwarfManagerListener {

    /**
     * A new dwarf has been added to the dwarf manager.
     * @param newDwarf the new dwarf
     */
    void dwarfAdded(IGameCharacter newDwarf);

    /**
     * A dwarf has been removed from the dwarf manager.
     * @param removedDwarf the removed dwarf
     */
    void dwarfRemoved(IGameCharacter removedDwarf);
}
