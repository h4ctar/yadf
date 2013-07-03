package simulation.military;

/**
 * A listener to the military manager, they're notified whenever a soldier is added or removed.
 */
public interface IMilitaryManagerListener {

    /**
     * A new soldier has been added.
     */
    void soldierAdded();

    /**
     * A soldier has been removed from the military.
     */
    void soldierRemoved();
}
