package simulation.item;


/**
 * Interface for a stock manager listener.
 */
public interface IStockManagerListener {
    /**
     * An item has been become available, i.e. it has just been added or it is not being used anymore.
     * @param availableItem the available item
     */
    void itemNowAvailable(Item availableItem);
}
