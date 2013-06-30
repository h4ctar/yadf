package simulation.item;

/**
 * Interface for a stock manager listener.
 */
public interface IStockManagerListener extends IContainerListener {

    /**
     * A new stockpile has been added.
     * @param stockpile the new stockpile
     */
    void stockpileAdded(Stockpile stockpile);

    /**
     * A stockpile has been removed.
     * @param stockpile the removed stockpile
     */
    void stockpileRemoved(Stockpile stockpile);
}
