package simulation;

import simulation.character.Dwarf;

/**
 * Interface for object that want to listen to a player.
 */
public interface IDwarfManagerListener {

    public void dwarfAdded(Dwarf dwarf);

    public void dwarfRemoved(Dwarf dwarf);
}
