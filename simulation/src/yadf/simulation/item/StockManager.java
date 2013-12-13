/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package yadf.simulation.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import yadf.logger.Logger;
import yadf.simulation.AbstractGameObject;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * The Class StockManager, manages items for a player including stockpiles.
 */
public class StockManager extends AbstractGameObject implements IStockManager, IGameObjectListener {

    /** The container component; contains unstored items. */
    private final ContainerComponent containerComponent = new ContainerComponent(this);

    /** The stockpiles owned by this stock manager. */
    private final List<Stockpile> stockpiles = new ArrayList<>();

    /** Listeners for add and remove of items and stockpiles. */
    private final List<IGameObjectManagerListener> managerListeners = new ArrayList<>();

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Adding item: " + item.getType());
        return containerComponent.addItem(item);
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Removing item: " + item.getType());
        boolean itemRemoved = false;
        itemRemoved = containerComponent.removeItem(item);
        if (!itemRemoved) {
            for (Stockpile stockpile : stockpiles) {
                itemRemoved = stockpile.removeItem(item);
                if (itemRemoved) {
                    break;
                }
            }
        }
        return itemRemoved;
    }

    /**
     * Gets all the unstored items.
     * @return A list of references to all the unstored items
     */
    @Override
    public List<Item> getItems() {
        return containerComponent.getItems();
    }

    @Override
    public Item getItem(final MapIndex mapIndex) {
        for (Item item : getItems()) {
            if (mapIndex.equals(item.getPosition())) {
                return item;
            }
        }
        for (Stockpile stockpile : stockpiles) {
            Item item = stockpile.getItem(mapIndex);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    /**
     * Notify all the listeners that a stockpile was added.
     * @param stockpile the stockpile that was added
     */
    private void notifyStockpileAdded(final Stockpile stockpile) {
        for (IGameObjectManagerListener managerListener : managerListeners) {
            managerListener.gameObjectAdded(stockpile, stockpiles.indexOf(stockpile));
        }
    }

    /**
     * Notify all the listeners that a stockpile was removed.
     * @param stockpile the stockpile that was removed
     * @param index index of the removed stockpile
     */
    private void notifyStockpileRemoved(final Stockpile stockpile, final int index) {
        for (IGameObjectManagerListener managerListener : managerListeners) {
            managerListener.gameObjectRemoved(stockpile, index);
        }
    }

    /**
     * Finds an item that is not stored, including containers.
     * @param itemTypes the item types to find
     * @return A reference to the found item, will be null if none could be found
     */
    @Override
    public Item getUnstoredItem(final Set<ItemType> itemTypes) {
        Item foundItem = null;
        for (Item item : getItems()) {
            if (item.canBeStored(itemTypes) && !item.isUsed()) {
                foundItem = item;
                break;
            }
        }
        return foundItem;
    }

    /**
     * Finds an item that is not stored, not including containers.
     * @param itemType the item type to find
     * @return A reference to the found item, will be null if none could be found
     */
    @Override
    public Item getUnstoredItem(final ItemType itemType) {
        for (Item item : getItems()) {
            if (itemType.equals(item.getType()) && !item.isUsed() && !item.isDeleted() && !item.isPlaced()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Item getItem(final String itemTypeName, final boolean used, final boolean placed) {
        Item unusedItem = containerComponent.getItem(itemTypeName, used, placed);
        if (unusedItem == null) {
            for (Stockpile stockpile : stockpiles) {
                unusedItem = stockpile.getItem(itemTypeName, used, placed);
                if (unusedItem != null) {
                    break;
                }
            }
        }
        return unusedItem;
    }

    @Override
    public Item getItemFromCategory(final String category, final boolean used, final boolean placed) {
        Item unusedItem = containerComponent.getItemFromCategory(category, used, placed);
        if (unusedItem == null) {
            for (Stockpile stockpile : stockpiles) {
                unusedItem = stockpile.getItemFromCategory(category, used, placed);
                if (unusedItem != null) {
                    break;
                }
            }
        }
        return unusedItem;
    }

    @Override
    public void addStockpile(final Stockpile stockpile) {
        // The stockpile needs to listen to all item types
        for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
            stockpile.addItemAvailableListener(itemType, containerComponent);
        }
        stockpiles.add(stockpile);
        notifyStockpileAdded(stockpile);
        stockpile.addGameObjectListener(this);
    }

    @Override
    public void removeStockpile(final Stockpile stockpile) {
        for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
            stockpile.removeItemAvailableListener(itemType, containerComponent);
        }
        int index = stockpiles.indexOf(stockpile);
        stockpiles.remove(stockpile);
        notifyStockpileRemoved(stockpile, index);
        stockpile.removeGameObjectListener(this);
    }

    @Override
    public Stockpile getStockpile(final int stockpileId) {
        for (Stockpile stockpile : stockpiles) {
            if (stockpile.getId() == stockpileId) {
                return stockpile;
            }
        }
        return null;
    }

    @Override
    public Stockpile getStockpile(final MapIndex mapIndex) {
        for (Stockpile stockpile : stockpiles) {
            MapArea area = stockpile.getArea();
            if (area.containesIndex(mapIndex)) {
                return stockpile;
            }
        }
        return null;
    }

    @Override
    public List<Stockpile> getStockpiles() {
        return stockpiles;
    }

    @Override
    public int getItemQuantity(final String category) {
        int count = containerComponent.getItemQuantity(category);
        for (Stockpile stockpile : stockpiles) {
            count += stockpile.getItemQuantity(category);
        }
        return count;
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        int count = containerComponent.getItemQuantity(itemType);
        for (Stockpile stockpile : stockpiles) {
            count += stockpile.getItemQuantity(itemType);
        }
        return count;
    }

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        managerListeners.add(listener);
        containerComponent.addGameObjectManagerListener(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        managerListeners.remove(listener);
        containerComponent.removeGameObjectManagerListener(listener);
    }

    @Override
    public void addItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.addItemAvailableListener(itemType, listener);
    }

    @Override
    public void addItemAvailableListenerListener(final String category, final IItemAvailableListener listener) {
        containerComponent.addItemAvailableListenerListener(category, listener);
    }

    @Override
    public void removeItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.removeItemAvailableListener(itemType, listener);
    }

    @Override
    public void removeItemAvailableListener(final String category, final IItemAvailableListener listener) {
        containerComponent.removeItemAvailableListener(category, listener);
    }

    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        assert stockpiles.contains(gameObject);
        removeStockpile((Stockpile) gameObject);
    }

    @Override
    public void gameObjectChanged(IGameObject gameObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // do nothing
    }
}
