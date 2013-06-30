package simulation.farm;

import java.util.Set;

import simulation.IPlayer;

/**
 * The farm manager.
 */
public interface IFarmManager {

    /**
     * Adds a farm.
     * @param farm the farm
     */
    void addFarm(final Farm farm);

    /**
     * Update all the farms.
     * @param player the player
     */
    void update(IPlayer player);

    /**
     * Gets all the farms.
     * @return the farms
     */
    Set<Farm> getFarms();

    /**
     * Add a new farm manager listener who will be notified when a farm is added or removed.
     * @param listener the new listener
     */
    void addListener(IFarmManagerListener listener);

    /**
     * Remove a farm manager listener.
     * @param listener the listener to remove
     */
    void removeListener(IFarmManagerListener listener);
}
