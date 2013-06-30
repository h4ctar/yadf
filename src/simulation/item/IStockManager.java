package simulation.item;

import java.util.Set;

import simulation.map.MapIndex;

/**
 * Interface for a stock manager.
 */
public interface IStockManager extends IContainer {

    /**
     * Finds an item that is not stored, including containers.
     * @param itemTypes the item types to find
     * @return A reference to the found item, will be null if none could be found
     */
    Item getUnstoredItem(Set<ItemType> itemTypes);

    /**
     * Finds an item that is not stored, including containers.
     * @param itemType the item type to find
     * @return A reference to the found item, will be null if none could be found
     */
    Item getUnstoredItem(ItemType itemType);

    /**
     * Gets references to all the stockpiles.
     * @return A vector of references to all the stockpiles
     */
    Set<Stockpile> getStockpiles();

    /**
     * Get an item located at a specific map index.
     * @param mapIndex the map index
     * @return the item, null if no item found
     */
    Item getItem(MapIndex mapIndex);

    /**
     * Add a new stockpile.
     * @param stockpile the new stockpile
     */
    void addStockpile(Stockpile stockpile);

    /**
     * Gets the stockpile at a specific map index.
     * @param mapIndex the index
     * @return the stockpile
     */
    Stockpile getStockpile(MapIndex mapIndex);

    /**
     * Gets the stockpile with a specific ID.
     * @param stockpileId the ID of the stockpile
     * @return the stockpile, null if no stockpile found
     */
    Stockpile getStockpile(int stockpileId);

    /**
     * Remove a stockpile from the stockmanager.
     * @param stockpile the stockpile to remove
     */
    void removeStockpile(Stockpile stockpile);

    /**
     * Add a listener that will be notified whenever a stockpile is added or removed.
     * @param listener the listener to add
     */
    void addListener(IStockManagerListener listener);

    /**
     * Remove a listener that was be notified whenever a stockpile is added or removed.
     * @param listener the listener to remove
     */
    void removeListener(IStockManagerListener listener);
}
