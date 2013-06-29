package simulation.item;

/**
 * Interface for a stock manager listener.
 */
public interface IStockManagerListener extends IContainerListener {
    void stockpileAdded(Stockpile stockpile);

    void stockpileRemoved(Stockpile stockpile);
}
