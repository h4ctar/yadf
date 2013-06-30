package simulation;

import simulation.character.IDwarfManager;
import simulation.farm.IFarmManager;
import simulation.item.IStockManager;
import simulation.job.IJobManager;
import simulation.room.IRoomManager;
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
     * Get the farm manager.
     * @return the farm manager
     */
    IFarmManager getFarmManager();

    /**
     * Get the room manager.
     * @return the room manager
     */
    IRoomManager getRoomManager();

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
}
