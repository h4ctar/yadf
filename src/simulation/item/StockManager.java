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
package simulation.item;

import java.util.LinkedHashSet;
import java.util.Set;

import logger.Logger;
import simulation.AbstractGameObject;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class StockManager, manages items for a player including stockpiles.
 */
public class StockManager extends AbstractGameObject implements IStockManager {

    /** The container component; contains unstored items. */
    private final ContainerComponent containerComponent = new ContainerComponent(this);

    /** The stockpiles owned by this stock manager. */
    private final Set<Stockpile> stockpiles = new LinkedHashSet<>();

    /** Listeners for add and remove of items and stockpiles. */
    private final Set<IStockManagerListener> managerListeners = new LinkedHashSet<>();

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Adding item: " + item.getType());
        boolean itemAdded = containerComponent.addItem(item);
        if (itemAdded) {
            notifyItemAdded(item);
        }
        return itemAdded;
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
        if (itemRemoved) {
            notifyItemRemoved(item);
        }
        return itemRemoved;
    }

    /**
     * Gets all the unstored items.
     * @return A list of references to all the unstored items
     */
    @Override
    public Set<Item> getItems() {
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
     * Notify all the listeners that an item has been added.
     * @param item the item that was added
     */
    private void notifyItemAdded(final Item item) {
        for (IStockManagerListener managerListener : managerListeners) {
            managerListener.itemAdded(item);
        }
    }

    /**
     * Notify all the listeners that an item was removed.
     * @param item the item that was removed
     */
    private void notifyItemRemoved(final Item item) {
        for (IStockManagerListener managerListener : managerListeners) {
            managerListener.itemRemoved(item);
        }
    }

    /**
     * Notify all the listeners that a stockpile was added.
     * @param stockpile the stockpile that was added
     */
    private void notifyStockpileAdded(final Stockpile stockpile) {
        for (IStockManagerListener managerListener : managerListeners) {
            managerListener.stockpileAdded(stockpile);
        }
    }

    /**
     * Notify all the listeners that a stockpile was removed.
     * @param stockpile the stockpile that was removed
     */
    private void notifyStockpileRemoved(final Stockpile stockpile) {
        for (IStockManagerListener managerListener : managerListeners) {
            managerListener.stockpileRemoved(stockpile);
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
    public Item getUnusedItem(final String itemTypeName) {
        Item unusedItem = containerComponent.getUnusedItem(itemTypeName);
        if (unusedItem == null) {
            for (Stockpile stockpile : stockpiles) {
                unusedItem = stockpile.getUnusedItem(itemTypeName);
                if (unusedItem != null) {
                    break;
                }
            }
        }
        return unusedItem;
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        Item unusedItem = containerComponent.getUnusedItemFromCategory(category);
        if (unusedItem == null) {
            for (Stockpile stockpile : stockpiles) {
                unusedItem = stockpile.getUnusedItemFromCategory(category);
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
            stockpile.addListener(itemType, containerComponent);
        }
        stockpiles.add(stockpile);
        notifyStockpileAdded(stockpile);
    }

    @Override
    public void removeStockpile(final Stockpile stockpile) {
        for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
            stockpile.removeListener(itemType, containerComponent);
        }
        stockpiles.remove(stockpile);
        notifyStockpileRemoved(stockpile);
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
    public Set<Stockpile> getStockpiles() {
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

    // TODO: move this into container component...?
    // is a stockpile an item?
    @Override
    public void addListener(final IStockManagerListener listener) {
        managerListeners.add(listener);
    }

    @Override
    public void removeListener(final IStockManagerListener listener) {
        managerListeners.remove(listener);
    }

    @Override
    public void addListener(final IContainerListener listener) {
        containerComponent.addListener(listener);
    }

    @Override
    public void removeListener(final IContainerListener listener) {
        containerComponent.removeListener(listener);
    }

    @Override
    public void addListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.addListener(itemType, listener);
    }

    @Override
    public void addListener(final String category, final IItemAvailableListener listener) {
        containerComponent.addListener(category, listener);
    }

    @Override
    public void removeListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.removeListener(itemType, listener);
    }

    @Override
    public void removeListener(final String category, final IItemAvailableListener listener) {
        containerComponent.removeListener(category, listener);
    }
}
