package yadf.simulation.item;

import yadf.simulation.AbstractGameObjectManager;

public class StockpileManager extends AbstractGameObjectManager<Stockpile> implements IStockpileManager {

    @Override
    public void removeItem(final Item item) {
        for (Stockpile stockpile : getGameObjects()) {
            stockpile.removeGameObject(item);
        }
    }

    @Override
    public Item getItem(final String itemTypeName) {
        Item foundItem = null;
        for (Stockpile stockpile : getGameObjects()) {
            foundItem = stockpile.getItem(itemTypeName, false);
            if (foundItem != null) {
                break;
            }
        }
        return foundItem;
    }

    @Override
    public Item getItem(final int id) {
        Item foundItem = null;
        for (Stockpile stockpile : getGameObjects()) {
            foundItem = stockpile.getGameObject(id);
            if (foundItem != null) {
                break;
            }
        }
        return foundItem;
    }

    @Override
    public Item getItemFromCategory(final String category) {
        Item foundItem = null;
        for (Stockpile stockpile : getGameObjects()) {
            foundItem = stockpile.getItemFromCategory(category, false);
            if (foundItem != null) {
                break;
            }
        }
        return foundItem;
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        int count = 0;
        for (Stockpile stockpile : getGameObjects()) {
            count += stockpile.getItemQuantity(itemType);
        }
        return count;
    }

    @Override
    public int getItemQuantity(final String category) {
        int count = 0;
        for (Stockpile stockpile : getGameObjects()) {
            count += stockpile.getItemQuantity(category);
        }
        return count;
    }
}
