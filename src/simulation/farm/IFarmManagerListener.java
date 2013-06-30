package simulation.farm;

/**
 * Listener for someone who wants to be notified when a farm is added or removed.
 */
public interface IFarmManagerListener {

    /**
     * A new farm has been added.
     * @param farm the new farm
     */
    void farmAdded(Farm farm);

    /**
     * A farm has been removed.
     * @param farm the removed farm
     */
    void farmRemoved(Farm farm);
}
