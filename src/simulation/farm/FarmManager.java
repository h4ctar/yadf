package simulation.farm;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IPlayer;

/**
 * The farm manager.
 */
public class FarmManager implements IFarmManager {

    /** The farms. */
    private final Set<Farm> farms = new CopyOnWriteArraySet<>();

    /** The farm manager listeners. */
    private final Set<IFarmManagerListener> listeners = new LinkedHashSet<>();

    /**
     * Adds a farm.
     * @param farm the farm
     */
    public void addFarm(final Farm farm) {
        farms.add(farm);
        for (IFarmManagerListener listener : listeners) {
            listener.farmAdded(farm);
        }
    }

    @Override
    public Set<Farm> getFarms() {
        return farms;
    }

    @Override
    public void update(final IPlayer player) {
        // TODO: Don't update farms every step
        for (Farm farm : farms) {
            farm.update(player);
        }
    }

    @Override
    public void addListener(final IFarmManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IFarmManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }
}
