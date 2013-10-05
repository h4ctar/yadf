package yadf.simulation.farm;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;

/**
 * The farm manager.
 */
public class FarmManager implements IFarmManager {

    /** The farms. */
    private final List<Farm> farms = new ArrayList<>();

    /** The farm manager listeners. */
    private final Set<IGameObjectManagerListener> listeners = new LinkedHashSet<>();

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
        farms.add(farm);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectAdded(farm, farms.indexOf(farm));
        }
    }

    @Override
    public List<Farm> getFarms() {
        return farms;
    }

    /**
     * Update all the farms.
     */
    public void update() {
        // TODO: Don't update farms every step
        for (Farm farm : farms) {
            farm.update();
        }
    }

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }
}
