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

import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import simulation.GameObject;
import simulation.Player;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.HaulJob;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class Stockpile.
 */
public class Stockpile extends GameObject {

    /** Is the position in the stockpile used. */
    private final boolean[][] used;

    /** A reference to the item at a particular position. */
    private final List<Item> items = new ArrayList<>();

    /** The area of the stockpile. */
    private final MapArea area;

    /** What item type the stockpile accepts. */
    private final List<ItemType> itemTypes = new ArrayList<>();

    /**
     * An array of haul tasks that have been created for this stockpile, its remembered so they can be canceled if the
     * stockpile is deleted or its items to collect changes.
     */
    private final List<HaulJob> haulJobs = new ArrayList<>();

    /** The listeners that are notified of changes in the stockpile. */
    private final List<IStockpileListener> listeners = new ArrayList<>();

    /**
     * Constructor for the stockpile.
     * @param areaTmp The area the stockpile will occupy
     */
    public Stockpile(final MapArea areaTmp) {
        area = areaTmp;
        used = new boolean[area.width][area.height];
        Logger.getInstance().log(this, "Stockpile id: " + getId());
    }

    /**
     * Sets an item so that it belongs to this stockpile.
     * @param item The item to be added
     */
    public void addItem(final Item item) {
        if (itemTypes.contains(item.getType())) {
            MapIndex pos = item.getPosition().sub(area.pos);
            used[pos.x][pos.y] = true;
            items.add(item);
        }

        item.setUsed(false);
    }

    /**
     * Add a new listener to this stockpile.
     * @param listener the new listener
     */
    public void addListener(final IStockpileListener listener) {
        listeners.add(listener);
    }

    /**
     * Cancel haul tasks.
     */
    public void cancelHaulTasks() {
        for (HaulJob haulTask : haulJobs) {
            haulTask.interrupt("The stockpile was deleted or no longer accepts this item type");
        }
    }

    /**
     * Get if the stockpile accepts a certain item type.
     * @param itemTypeName the item type name
     * @return the accepts item type
     */
    public boolean getAcceptsItemType(final String itemTypeName) {
        for (ItemType itemType : itemTypes) {
            if (itemType.equals(itemTypeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the area of the stockpile.
     * @return The area of the stockpile
     */
    public MapArea getArea() {
        return area;
    }

    /**
     * Gets the item count.
     * @param itemName the item name
     * @return the item count
     */
    public int getItemCount(final String itemName) {
        int count = 0;

        for (Item item : items) {
            if (item.getType().equals(itemName)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the item count in category.
     * @param categoryName the category name
     * @return the item count in category
     */
    public int getItemCountInCategory(final String categoryName) {
        int count = 0;
        for (Item item : items) {
            if (item.getType().category.equals(categoryName)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets all the stored items.
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Returns the types of item that this stockpile will accept.
     * @return the types of item that this stockpile will accept
     */
    public List<ItemType> getItemTypes() {
        return itemTypes;
    }

    /**
     * Gets the unused item.
     * @param itemTypeName the item type name
     * @return the unused item
     */
    public Item getUnusedItem(final String itemTypeName) {
        for (Item item : items) {
            if (item.getType().equals(itemTypeName) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                return item;
            }
        }
        return null;
    }

    /**
     * Checks for all item types in category.
     * @param categoryName the category name
     * @return true, if successful
     */
    public boolean hasAllItemTypesInCategory(final String categoryName) {
        int count = 0;
        for (ItemType itemType : itemTypes) {
            if (itemType.category.equals(categoryName)) {
                // assuming the same item type is not in there twice
                count++;
            }
        }

        return count == ItemTypeManager.getInstance().getNumberOfItemTypesInCategory(categoryName);
    }

    /**
     * Removes the item.
     * 
     * @param item the item
     */
    public void removeItemFromStorage(final Item item) {
        MapIndex pos = item.getPosition().sub(area.pos);
        used[pos.x][pos.y] = false;

        if (!items.remove(item)) {
            Logger.getInstance().log(this, "Item removal failed(" + pos.x + ", " + pos.y + ") - " + items.size());
        }
    }

    /**
     * Sets the type of item that this stockpile will collect.
     * @param itemTypeName the item type name
     * @param accept true to accept the item type
     * @param stockManager the stock manager, used to add items that have been removed from the stockpile
     */
    public void setItemType(final String itemTypeName, final boolean accept, final StockManager stockManager) {
        ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
        if (accept) {
            if (!itemTypes.contains(itemType)) {
                itemTypes.add(itemType);
            }
        } else {
            itemTypes.remove(itemType);
            for (HaulJob haulTask : haulJobs) {
                if (haulTask.getItem().getType().equals(itemTypeName)) {
                    haulTask.interrupt("The stockpile was deleted or no longer accepts this item type");
                    MapIndex pos = haulTask.getDropPosition().sub(area.pos);
                    used[pos.x][pos.y] = false;
                }
            }
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (!itemTypes.contains(item.getType())) {
                stockManager.addItem(item);
                MapIndex pos = item.getPosition().sub(area.pos);
                used[pos.x][pos.y] = false;
                i--;
            }
        }
    }

    /**
     * Tries to fill the stockpile, if any positions are not taken it will try to find and unstored item and add a job
     * to haul it into position.
     * @param player the player
     */
    public void update(final Player player) {
        boolean changed = false;
        // Remove done haul tasks
        for (int i = 0; i < haulJobs.size(); i++) {
            if (haulJobs.get(i).isDone()) {
                haulJobs.remove(i);
            }
        }
        // Create haul tasks to fill up the stockpile
        if (!itemTypes.isEmpty()) {
            for (int x = 0; x < area.width; x++) {
                for (int y = 0; y < area.height; y++) {
                    if (!used[x][y]) {
                        Item item = player.getStockManager().getUnstoredItem(itemTypes);
                        if (item != null) {
                            used[x][y] = true;
                            HaulJob haulJob = new HaulJob(item, true, area.pos.add(x, y, 0));
                            haulJobs.add(haulJob);
                            player.getJobManager().addJob(haulJob);
                        }
                    }
                }
            }
        }
        // Remove items marked for removal
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getRemove()) {
                MapIndex pos = item.getPosition().sub(area.pos);
                used[pos.x][pos.y] = true;
                items.remove(i);
                i--;
                changed = true;
            }
        }
        if (changed) {
            notifyListeners();
        }
    }

    /**
     * Notify the listeners that something has changed for the stockpile.
     */
    private void notifyListeners() {
        for (IStockpileListener listener : listeners) {
            listener.update();
        }
    }
}
