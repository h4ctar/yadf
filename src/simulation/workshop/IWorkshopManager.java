package simulation.workshop;

import java.util.Set;

import simulation.farm.IGameObjectManager;
import simulation.map.MapIndex;

/**
 * Interface for a workshop manager.
 */
public interface IWorkshopManager extends IGameObjectManager {

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
}
