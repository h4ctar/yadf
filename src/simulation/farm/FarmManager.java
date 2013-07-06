package simulation.farm;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObjectManagerListener;
import simulation.IPlayer;

/**
 * The farm manager.
 */
public class FarmManager implements IFarmManager {

    /** The farms. */
    private final Set<Farm> farms = new CopyOnWriteArraySet<>();

    /** The farm manager listeners. */
    private final Set<IGameObjectManagerListener> listeners = new LinkedHashSet<>();

    /**
     * Adds a farm.
     * @param farm the farm
     */
    public void addFarm(final Farm farm) {
        farms.add(farm);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectAdded(farm);
        }
    }

    @Override
    public Set<Farm> getFarms() {
        return farms;
    }

    /**
     * Update all the farms.
     * @param player the player
     */
    public void update(final IPlayer player) {
        // TODO: Don't update farms every step
        for (Farm farm : farms) {
            farm.update(player);
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
