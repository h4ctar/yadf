package simulation.item;

import java.util.Set;

import simulation.IGameObject;

/**
 * Interface for a container.
 */
public interface IContainer extends IGameObject {
    /**
     * Add an item to this container.
     * @param item the item to add
     * @return true if the item was added
     */
    boolean addItem(Item item);

    /**
     * Removes the item from the container, or any sub containers.
     * @param item the item to be removed
     * @return true if the item was found and removed
     */
    boolean removeItem(Item item);

    /**
     * Finds an item that is unused but does not remove it from the container. Does not set the item as used.
     * @param itemTypeName the type of item to find
     * @return a reference to the found item, will be null if none could be found
     */
    Item getUnusedItem(String itemTypeName);

    /**
     * Finds an item from a category. Does not set the item as used.
     * @param category the category name
     * @return the item from category
     */
    Item getUnusedItemFromCategory(String category);

    /**
     * Gets all the items.
     * @return A list of references to all the items
     */
    Set<Item> getItems();
}
