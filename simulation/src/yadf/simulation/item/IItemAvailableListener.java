package yadf.simulation.item;

/**
 * Interface for a someone who wants to be notified when an item becomes free.
 */
public interface IItemAvailableListener {

    /**
     * The item is available.
     * @param availableItem the item
     * @param container the container that sent this notification, null if the item sent it
     */
    void itemAvailable(Item availableItem, IContainer container);
}
