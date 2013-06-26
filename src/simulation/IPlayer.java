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

    /**
     * Add a new workshop.
     * @param workshop the new workshop
     */
    void addWorkshop(Workshop workshop);

    /**
     * Remove a workshop.
     * @param workshop the workshop to remove
     */
    void removeWorkshop(Workshop workshop);

    /**
     * Get all the rooms.
     * @return all the rooms
     */
    Set<Room> getRooms();

    /**
     * Get the room at a specific map index.
     * @param position the map index
     * @return the room
     */
    Room getRoom(MapIndex position);

    /**
     * Remove a room.
     * @param room the room to remove
     */
    void removeRoom(Room room);
}
