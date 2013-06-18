package simulation;

import java.util.Set;

import simulation.character.IDwarfManager;
import simulation.item.IStockManager;
import simulation.job.IJobManager;
import simulation.map.MapIndex;
import simulation.room.Room;
import simulation.workshop.Workshop;

/**
 * Interface of a player.
 */
public interface IPlayer {
    /**
     * Get the players stock manager.
     * @return the stock manager
     */
    IStockManager getStockManager();

    /**
     * Get the players job manager.
     * @return the job manager
     */
    IJobManager getJobManager();

    /**
     * Gets the dwarf manager.
     * @return the dwarf manager
     */
    IDwarfManager getDwarfManager();

    void addWorkshop(Workshop workshop);

    Set<Room> getRooms();

    Room getRoom(MapIndex position);

    Region getRegion();
}
