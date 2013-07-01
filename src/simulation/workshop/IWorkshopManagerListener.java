package simulation.workshop;

/**
 * Interface for a workshop manager listener, will be notified when a workshop is added or removed.
 */
public interface IWorkshopManagerListener {

    /**
     * A new workshop has been added.
     * @param workshop the new workshop
     */
    void workshopAdded(Workshop workshop);

    /**
     * A workshop has been removed.
     * @param workshop the removed workshop
     */
    void workshopRemoved(Workshop workshop);
}
