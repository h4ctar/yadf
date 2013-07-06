package simulation.item;

import java.util.Set;

import simulation.IGameObject;
import simulation.farm.IGameObjectManager;

/**
 * Interface for a container.
 */
public interface IContainer extends IGameObject, IGameObjectManager {
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
     * Finds an item but does not remove it from the container and does not set the item as used.
     * @param itemTypeName the type of item to find
     * @param used true to only find used items
     * @param placed true to only find placed items
     * @return a reference to the found item, will be null if none could be found
     */
    Item getItem(String itemTypeName, boolean used, boolean placed);

    /**
     * Finds an item from a category but does not remove it from the container and does not set the item as used.
     * @param category the category name
     * @param used true to only find used items
     * @param placed true to only find placed items
     * @return the item from category
     */
    Item getItemFromCategory(String category, boolean used, boolean placed);

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
     * Add a listener to the container that will be notified when an item of a specific type becomes available.
     * @param itemType the type of item to be notified for
     * @param listener the new listener
     */
    void addItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener);

    /**
     * Add a listener to the container that listens for available items of all item types in one category.
     * @param category the category
     * @param listener the new listener
     */
    void addItemAvailableListenerListener(final String category, IItemAvailableListener listener);

    /**
     * Remove a listener from a particular item type.
     * @param itemType the type of item to stop listening for
     * @param listener the listener to remove
     */
    void removeItemAvailableListener(ItemType itemType, IItemAvailableListener listener);

    /**
     * Remove a listener from listening to all item types from one category.
     * @param category the category
     * @param listener the listener to remove
     */
    void removeItemAvailableListener(final String category, IItemAvailableListener listener);
}
