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
     * Gets all the items.
     * @return A list of references to all the items
     */
    Set<Item> getItems();

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

    /**
     * Add a listener to the container that will be notified whenever an item is added or removed.
     * @param listener the listener to add
     */
    void addListener(final IContainerListener listener);

    /**
     * Remove a listener from the container that was be notified whenever an item is added or removed.
     * @param listener the listener to remove
     */
    void removeListener(final IContainerListener listener);

    /**
     * Add a listener to the container that will be notified when an item of a specific type becomes available.
     * @param itemType the type of item to be notified for
     * @param listener the new listener
     */
    void addListener(final ItemType itemType, final IItemAvailableListener listener);

    /**
     * Add a listener to the container that listens for available items of all item types in one category.
     * @param category the category
     * @param listener the new listener
     */
    void addListener(final String category, IItemAvailableListener listener);

    /**
     * Remove a listener from a particular item type.
     * @param itemType the type of item to stop listening for
     * @param listener the listener to remove
     */
    void removeListener(ItemType itemType, IItemAvailableListener listener);

    /**
     * Remove a listener from listening to all item types from one category.
     * @param category the category
     * @param listener the listener to remove
     */
    void removeListener(final String category, IItemAvailableListener listener);
}
