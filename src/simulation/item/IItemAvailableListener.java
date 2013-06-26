package simulation.item;

/**
 * Interface for a someone who wants to be notified when an item becomes free.
 */
public interface IItemAvailableListener {

    /**
     * The item is available.
     * @param item the item
     */
    void itemAvailable(Item item);
}
