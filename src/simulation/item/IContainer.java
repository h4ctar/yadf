package simulation.item;

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
     * Finds an item that is unused.
     * @param itemTypeName The type of item to find
     * @return A reference to the found item, will be null if none could be found
     */
    Item getUnusedItem(String itemTypeName);

    /**
     * Finds an item from a category.
     * @param category the category name
     * @return the item from category
     */
    Item getUnusedItemFromCategory(String category);
}
