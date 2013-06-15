package simulation.stock;

import java.util.List;

import simulation.item.IContainer;
import simulation.item.Item;
import simulation.item.ItemType;

public interface IStockManager extends IContainer {

    /**
     * Add a listener to the stock manager.
     * @param itemType the type of item to be notified for
     * @param listener the new listener
     */
    void addListener(final ItemType itemType, final IStockManagerListener listener);

    void removeListener(ItemType itemType, IStockManagerListener listener);

    /**
     * Remove a listener from listening to all item types
     * @param listener the listener to remove
     */
    void removeListener(IStockManagerListener listener);

    Item getUnstoredItem(List<ItemType> itemType);
}
