package simulation;

import simulation.job.IJobManager;
import simulation.stock.IStockManager;

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
     * Add a listener to this player, they will be notified when...
     * @param listener
     */
    void addListener(IPlayerListener listener);
}
