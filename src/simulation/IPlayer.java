package simulation;

import simulation.character.IDwarfManager;
import simulation.farm.IFarmManager;
import simulation.item.IStockManager;
import simulation.job.IJobManager;
import simulation.map.MapIndex;
import simulation.military.IMilitaryManager;
import simulation.room.IRoomManager;
import simulation.workshop.IWorkshopManager;

/**
 * Interface of a player.
 */
public interface IPlayer extends IGameObject {
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
     * Get the workshop manager.
     * @return the workshop manager
     */
    IWorkshopManager getWorkshopManager();

    /**
     * Get the military manager.
     * @return the military manager
     */
    IMilitaryManager getMilitaryManager();

    /**
     * Update.
     */
    void update();

    /**
     * Setup.
     * @param regionTmp the region that this player is in
     * @param embarkPosition the embark position
     * @param numberOfStartingDwarfs the number of starting dwarfs
     */
    void setup(final IRegion regionTmp, final MapIndex embarkPosition, final int numberOfStartingDwarfs);
}
