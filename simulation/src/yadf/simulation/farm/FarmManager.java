package yadf.simulation.farm;

import yadf.simulation.AbstractGameObjectManager;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;

/**
 * The farm manager.
 */
public class FarmManager extends AbstractGameObjectManager<Farm> implements IFarmManager {

    /** The player that this manager belongs to. */
    private final IPlayer player;

    /**
     * Constructor.
     * @param playerTmp the player that this manager belongs to.
     */
    public FarmManager(final IPlayer playerTmp) {
        player = playerTmp;
    }

    /**
     * Adds a new farm.
     * @param area the map area
     */
    public void addNewFarm(final MapArea area) {
        Farm farm = new Farm(area, player);
        addGameObject(farm);
    }

    /**
     * Update all the farms.
     */
    public void update() {
        // TODO: Don't update farms every step
        for (Farm farm : getGameObjects()) {
            farm.update();
        }
    }
}
