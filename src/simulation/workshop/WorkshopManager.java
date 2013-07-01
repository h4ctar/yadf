package simulation.workshop;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObject;
import simulation.IGameObjectListener;
import simulation.map.MapIndex;

/**
 * The workshop manager.
 */
public class WorkshopManager implements IWorkshopManager, IGameObjectListener {

    /** The workshops. */
    private final Set<Workshop> workshops = new CopyOnWriteArraySet<>();

    /** The workshop manager listeners. */
    private final Set<IWorkshopManagerListener> listeners = new LinkedHashSet<>();

    @Override
    public void addWorkshop(final Workshop workshop) {
        assert !workshops.contains(workshop);
        workshop.addGameObjectListener(this);
        workshops.add(workshop);
        for (IWorkshopManagerListener listener : listeners) {
            listener.workshopAdded(workshop);
        }
    }

    @Override
    public void removeWorkshop(final Workshop workshop) {
        assert workshops.contains(workshop);
        workshop.removeGameObjectListener(this);
        workshops.remove(workshop);
        for (IWorkshopManagerListener listener : listeners) {
            listener.workshopRemoved(workshop);
        }
    }

    @Override
    public Workshop getWorkshop(final int workshopId) {
        for (Workshop workshop : workshops) {
            if (workshop.getId() == workshopId) {
                return workshop;
            }
        }
        return null;
    }

    @Override
    public Workshop getWorkshop(final MapIndex index) {
        for (Workshop workshop : workshops) {
            if (workshop.hasIndex(index)) {
                return workshop;
            }
        }
        return null;
    }

    @Override
    public Set<Workshop> getWorkshops() {
        return workshops;
    }

    @Override
    public void update() {
        for (Workshop workshop : workshops) {
            workshop.update();
        }
    }

    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        removeWorkshop((Workshop) gameObject);
    }

    @Override
    public void addListener(final IWorkshopManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IWorkshopManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }
}
