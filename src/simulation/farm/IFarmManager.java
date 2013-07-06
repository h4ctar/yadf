package simulation.farm;

import java.util.Set;

/**
 * The farm manager.
 */
public interface IFarmManager extends IGameObjectManager {

    /**
     * Adds a farm.
     * @param farm the farm
     */
    void addFarm(final Farm farm);

    /**
     * Gets all the farms.
     * @return the farms
     */
    Set<Farm> getFarms();
}
