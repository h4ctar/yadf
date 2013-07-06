package simulation.workshop;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObjectListener;
import simulation.IGameObjectManagerListener;
import simulation.map.MapIndex;

/**
 * The workshop manager.
 */
public class WorkshopManager implements IWorkshopManager, IGameObjectListener {

    /** The workshops. */
    private final Set<IWorkshop> workshops = new CopyOnWriteArraySet<>();

    /** The workshop manager listeners. */
    private final Set<IGameObjectManagerListener> listeners = new LinkedHashSet<>();

    @Override
    public void addWorkshop(final IWorkshop workshop) {
        assert !workshops.contains(workshop);
        workshop.addGameObjectListener(this);
        workshops.add(workshop);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectAdded(workshop);
        }
    }

    @Override
    public void removeWorkshop(final IWorkshop workshop) {
        assert workshops.contains(workshop);
        workshop.removeGameObjectListener(this);
        workshops.remove(workshop);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectRemoved(workshop);
        }
    }

    @Override
    public IWorkshop getWorkshop(final int workshopId) {
        for (IWorkshop workshop : workshops) {
            if (workshop.getId() == workshopId) {
                return workshop;
            }
        }
        return null;
    }

    @Override
    public IWorkshop getWorkshop(final MapIndex index) {
        for (IWorkshop workshop : workshops) {
            if (workshop.hasIndex(index)) {
                return workshop;
            }
        }
        return null;
    }

    @Override
    public Set<IWorkshop> getWorkshops() {
        return workshops;
    }

    @Override
    public void update() {
        for (IWorkshop workshop : workshops) {
            workshop.update();
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

    @Override
    public void gameObjectDeleted(final Object gameObject) {
        assert workshops.contains(gameObject);
        removeWorkshop((Workshop) gameObject);
    }
}
