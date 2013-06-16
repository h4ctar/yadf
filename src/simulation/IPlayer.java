package simulation;

import simulation.character.IDwarfManager;
import simulation.item.IStockManager;
import simulation.job.IJobManager;

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
}
