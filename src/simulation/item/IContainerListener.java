package simulation.item;

/**
 * Interface for container listeners to implement.
 * <p>
 * Container listeners are notified whenever an item is added or removed from the container.
 */
public interface IContainerListener {

    /**
     * An item has been added.
     * @param item the new item
     */
    void itemAdded(Item item);

    /**
     * An item has been removed.
     * @param item the removed item
     */
    void itemRemoved(Item item);
}
