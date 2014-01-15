package yadf.simulation.item;

import java.util.Set;

import yadf.simulation.IGameObjectManager;

/**
 * Interface for an Item Manager.
 */
public interface IItemManager extends IGameObjectManager<Item> {

    /**
     * Finds an item but does not remove it from the container and does not set the item as used.
     * @param itemTypeName the type of item to find
     * @param placed true to only find placed items
     * @return a reference to the found item, will be null if none could be found
     */
    Item getItem(String itemTypeName, boolean placed);

    Item getItem(Set<ItemType> itemTypes);

    /**
     * Finds an item from a category but does not remove it from the container and does not set the item as used.
     * @param category the category name
     * @param placed true to only find placed items
     * @return the item from category
     */
    Item getItemFromCategory(String category, boolean placed);

    /**
     * Get the number of items in a category. From this container and any sub containers.
     * @param category the category
     * @return the number of items in the category
     */
    int getItemQuantity(String category);

    /**
     * Gets the item count of a particular item type. From this container and any sub containers.
     * @param itemType the item type
     * @return the item count
     */
    int getItemQuantity(ItemType itemType);
}
