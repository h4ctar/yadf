package yadf.simulation.farm;

import java.util.List;

import yadf.simulation.IGameObjectManager;
import yadf.simulation.IPlayerComponent;
import yadf.simulation.map.MapArea;

/**
 * The farm manager.
 */
public interface IFarmManager extends IGameObjectManager, IPlayerComponent {

    /**
     * Adds a new farm.
     * @param area the area of the farm
     */
    void addNewFarm(final MapArea area);

    /**
     * Gets all the farms.
     * @return the farms
     */
    List<Farm> getFarms();
}
