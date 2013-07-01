package simulation.workshop;

import java.util.Set;

import simulation.map.MapIndex;

/**
 * Interface for a workshop manager.
 */
public interface IWorkshopManager {

    /**
     * Add a new workshop.
     * @param workshop the new workshop
     */
    void addWorkshop(IWorkshop workshop);

    /**
     * Remove a workshop.
     * @param workshop the workshop to remove
     */
    void removeWorkshop(IWorkshop workshop);

    /**
     * Gets the workshops.
     * @return the workshops
     */
    Set<IWorkshop> getWorkshops();

    /**
     * Gets the workshop.
     * @param workshopId the workshop id
     * @return the workshop
     */
    IWorkshop getWorkshop(final int workshopId);

    /**
     * Get a workshop at a specific index.
     * @param index the index to look for a workshop
     * @return the workshop
     */
    IWorkshop getWorkshop(final MapIndex index);

    /**
     * Update all the workshops.
     */
    void update();

    /**
     * Add a new workshop manager listener that will be notified whenever a workshop is added or removed.
     * @param listener the new listener
     */
    void addListener(IWorkshopManagerListener listener);

    /**
     * Remove a workshop manager listener that was notified whenever a workshop was added or removed.
     * @param listener the listener to remove
     */
    void removeListener(IWorkshopManagerListener listener);
}
