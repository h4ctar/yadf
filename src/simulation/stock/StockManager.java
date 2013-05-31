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
package simulation.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import logger.Logger;
import simulation.Player;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class StockManager, manages items for a player including stockpiles.
 */
public class StockManager implements Serializable {

    /** The stockpiles owned by this stock manager. */
    private final List<Stockpile> stockpiles = new ArrayList<>();

    /** All the items looked after by this stock manager. */
    private final List<Item> items = new CopyOnWriteArrayList<>();

    /** Listeners to this stock manager. */
    private final List<IStockManagerListener> listeners = new ArrayList<>();

    /**
     * Add a new item to the stock manager.
     * @param item The item to add
     */
    public void addItem(final Item item) {
        items.add(item);
        notifyListeners();
    }

    /**
     * Add a listener to the stock manager.
     * @param listener the new listener
     */
    public void addListener(final IStockManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Adds a new stockpile to this stock manager.
     * @param stockpile The new stockpile
     */
    public void addStockpile(final Stockpile stockpile) {
        stockpiles.add(stockpile);
    }

    /**
     * Gets the item count.
     * @param itemType the item type
     * @return the item count
     */
    public int getItemCount(final ItemType itemType) {
        int count = 0;

        for (Item item : items) {
            if (item.getType().equals(itemType)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get the number of items in a category.
     * @param category the category
     * @return the number of items in the category
     */
    public int getItemCount(final String category) {
        int count = 0;

        for (Item item : items) {
            if (item.getType().category.equals(category)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets all the items.
     * @return A vector of references to all the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Gets the stockpile.
     * @param stockpileId the stockpile id
     * @return the stockpile
     */
    public Stockpile getStockpile(final int stockpileId) {
        for (Stockpile stockpile : stockpiles) {
            if (stockpile.getId() == stockpileId) {
                return stockpile;
            }
        }

        return null;
    }

    /**
     * Gets the stockpile.
     * @param index the index
     * @return the stockpile
     */
    public Stockpile getStockpile(final MapIndex index) {
        for (Stockpile stockpile : stockpiles) {
            MapArea area = stockpile.getArea();
            if (index.x >= area.pos.x && index.x < area.pos.x + area.width && index.y >= area.pos.y
                    && index.y < area.pos.y + area.height && area.pos.z == index.z) {
                return stockpile;
            }
        }
        return null;
    }

    /**
     * Gets references to all the stockpiles.
     * @return A vector of references to all the stockpiles
     */
    public List<Stockpile> getStockpiles() {
        return stockpiles;
    }

    /**
     * Finds an item that is not in a stockpile, used by stockpile.update, also flags the item as "used".
     * @param itemTypes the item types
     * @return A reference to the found item, will be null if none could be found
     */
    public Item getUnstoredItem(final List<ItemType> itemTypes) {
        for (Item item : items) {
            if (itemTypes.contains(item.getType()) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                item.setUsed(true);
                return item;
            }
        }
        return null;
    }

    /**
     * Finds an item that is unused, flags the item as "used", and if it was in a stockpile, the item is removed from
     * the stockpile.
     * @param itemTypeName The type of item to find
     * @return A reference to the found item, will be null if none could be found
     */
    public Item getUnusedItem(final String itemTypeName) {
        for (Item item : items) {
            if (item.getType().equals(itemTypeName) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                return item;
            }
            Item contentItem = item.getUnusedItem(itemTypeName);
            if (contentItem != null) {
                // TODO: is this the right time to add the item to the stock manager? maybe it should just mark it as
                // used and wait for the dwarf to take it out of the container, same for stockpile.
                addItem(contentItem);
                return contentItem;
            }
        }
        for (Stockpile stockpile : stockpiles) {
            Item item = stockpile.getUnusedItem(itemTypeName);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the item from category.
     * @param category the category name
     * @return the item from category
     */
    public Item getUnusedItemFromCategory(final String category) {
        for (Stockpile stockpile : stockpiles) {
            for (Item item : stockpile.getItems()) {
                if (item.getType().category.equals(category) && !item.isUsed() && !item.getRemove()
                        && !item.isPlaced()) {
                    item.setUsed(true);
                    return item;
                }
            }
        }
        for (Item item : this.items) {
            if (item.getType().category.equals(category) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                item.setUsed(true);
                return item;
            }
            Item contentItem = item.getUnusedItemFromCategory(category);
            if (contentItem != null) {
                contentItem.setUsed(true);
                return contentItem;
            }
        }

        return null;
    }

    /**
     * Removes the item from storage.
     * @param item the item
     */
    public void removeItemFromStorage(final Item item) {
        // TODO: change this to loop through all stockpiles and ask them to remove item until it finds the right one.
        Stockpile stockpile = getStockpile(item.getPosition());
        if (stockpile != null) {
            stockpile.removeItemFromStorage(item);
            return;
        }
        for (Item container : items) {
            if (container.removeItemFromContainer(item)) {
                return;
            }
        }
    }

    /**
     * Store item in a stockpile if one exists.
     * @param item the item
     */
    // TODO: remove this method
    public void storeItemInStockpile(final Item item) {
        items.remove(item);
        Stockpile stockpile = getStockpile(item.getPosition());

        if (stockpile != null) {
            stockpile.addItem(item);
        } else {
            Logger.getInstance().log(this, "Warning: Can't store item, not in stockpile");
        }
    }

    /**
     * Updates all the stockpiles, and removes items that have been tagged for removal.
     * @param player the player
     */
    public void update(final Player player) {
        // update the stockpiles and sum how many items they need to be full
        for (Stockpile stockpile : stockpiles) {
            stockpile.update(player);
        }

        // delete the stockpiles that need to be removed
        for (int i = 0; i < stockpiles.size(); i++) {
            if (stockpiles.get(i).getRemove()) {
                items.addAll(stockpiles.get(i).getItems());
                stockpiles.remove(i);
                i--;
            }
        }

        // delete the items that need to be removed
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getRemove()) {
                items.remove(i);
                i--;
                notifyListeners();
            }
        }
    }

    /**
     * Notify all listeners that something has changed in the stock manager.
     */
    private void notifyListeners() {
        for (IStockManagerListener listener : listeners) {
            listener.stockManagerChanged();
        }
    }
}
