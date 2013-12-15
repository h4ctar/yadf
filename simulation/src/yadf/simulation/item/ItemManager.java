package yadf.simulation.item;

import yadf.simulation.AbstractGameObjectManager;

/**
 * Item Manager.
 */
public class ItemManager extends AbstractGameObjectManager<Item> implements IItemManager {

    @Override
    public Item getItem(final String itemTypeName, final boolean placed) {
        Item foundItem = null;
        for (Item item : getGameObjects()) {
            if (item.getType().name.equals(itemTypeName) && item.isAvailable() && item.isPlaced() == placed
                    && !item.isDeleted()) {
                foundItem = item;
                break;
            }
        }
        return foundItem;
    }

    @Override
    public Item getItemFromCategory(final String category, final boolean placed) {
        Item foundItem = null;
        for (Item item : getGameObjects()) {
            if (item.getType().category.equals(category) && !item.isDeleted() && item.isAvailable()
                    && item.isPlaced() == placed) {
                foundItem = item;
                break;
            }
        }
        return foundItem;
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        int count = 0;
        for (Item item : getGameObjects()) {
            if (item.getType().equals(itemType)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getItemQuantity(final String category) {
        int count = 0;
        for (Item item : getGameObjects()) {
            if (item.getType().category.equals(category)) {
                count++;
            }
        }
        return count;
    }
}
