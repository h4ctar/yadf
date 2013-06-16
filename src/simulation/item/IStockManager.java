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

    Item getUnstoredItem(ItemType itemType);

    /**
     * Gets references to all the stockpiles.
     * @return A vector of references to all the stockpiles
     */
    Set<Stockpile> getStockpiles();

    /**
     * Get the number of items in a category.
     * @param category the category
     * @return the number of items in the category
     */
    int getItemQuantity(String category);

    /**
     * Gets the item count.
     * @param itemType the item type
     * @return the item count
     */
    int getItemQuantity(ItemType itemType);

    /**
     * Add a listener to the container.
     * @param itemType the type of item to be notified for
     * @param listener the new listener
     */
    void addListener(final ItemType itemType, final IStockManagerListener listener);

    /**
     * Add a listener to the container that listens to all item types.
     * @param listener the new listener
     */
    void addListener(IStockManagerListener listener);

    /**
     * Remove a listener from a particular item type.
     * @param itemType the type of item to stop listening for
     * @param listener the listener to remove
     */
    void removeListener(ItemType itemType, IStockManagerListener listener);

    /**
     * Remove a listener from listening to all item types.
     * @param listener the listener to remove
     */
    void removeListener(IStockManagerListener listener);

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
}
