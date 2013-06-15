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
import simulation.AbstractGameObject;
import simulation.Player;
import simulation.item.ContainerItem;
import simulation.item.IContainer;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.HaulJob;
import simulation.job.IJob;
import simulation.job.IJobListener;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class Stockpile.
 */
public class Stockpile extends AbstractGameObject implements IContainer, IJobListener, IStockManagerListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 4376721656018948647L;

    /** Is the position in the stockpile used. */
    private final boolean[][] used;

    /** A reference to the item at a particular position. */
    private final List<Item> items = new ArrayList<>();

    /** The area of the stockpile. */
    private final MapArea area;

    /** What item type the stockpile accepts. */
    private final List<ItemType> acceptableItemTypes = new ArrayList<>();

    /** The player that this stockpile belongs to. */
    private final Player player;

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
     * @param playerTmp the player the stockpile belongs to
     */
    public Stockpile(final MapArea areaTmp, final Player playerTmp) {
        area = areaTmp;
        player = playerTmp;
        used = new boolean[area.width][area.height];
        Logger.getInstance().log(this, "New stockpile id: " + getId());
    }

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Adding item - itemType: " + item.getType());
        boolean itemAdded = false;
        if (item.canBeStored(acceptableItemTypes)) {
            MapIndex pos = item.getPosition().sub(area.pos);
            used[pos.x][pos.y] = true;
            items.add(item);
            notifyListeners();
            itemAdded = true;
        } else {
            Logger.getInstance().log(this, "Stockpile does not accept this item type", true);
        }
        return itemAdded;
    }

    /**
     * Add a new listener to this stockpile.
     * @param listener the new listener
     */
    public void addListener(final IStockpileListener listener) {
        listeners.add(listener);
    }

    /**
     * Get if the stockpile accepts a certain item type.
     * @param itemTypeName the item type name
     * @return the accepts item type
     */
    public boolean getAcceptsItemType(final String itemTypeName) {
        for (ItemType itemType : acceptableItemTypes) {
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
        return acceptableItemTypes;
    }

    @Override
    public Item getUnusedItem(final String itemTypeName) {
        for (Item item : items) {
            if (item.getType().equals(itemTypeName) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                return item;
            }
            if (item instanceof ContainerItem) {
                Item contentItem = ((ContainerItem) item).getUnusedItem(itemTypeName);
                if (contentItem != null) {
                    return contentItem;
                }
            }
        }
        return null;
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        for (Item item : items) {
            if (item.getType().category.equals(category) && !item.isUsed() && !item.getRemove() && !item.isPlaced()) {
                return item;
            }
            if (item instanceof ContainerItem) {
                Item contentItem = ((ContainerItem) item).getUnusedItemFromCategory(category);
                if (contentItem != null) {
                    return contentItem;
                }
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
        for (ItemType itemType : acceptableItemTypes) {
            if (itemType.category.equals(categoryName)) {
                // assuming the same item type is not in there twice
                count++;
            }
        }
        return count == ItemTypeManager.getInstance().getNumberOfItemTypesInCategory(categoryName);
    }

    @Override
    public void jobChanged(final IJob job) {
        if (job.isDone()) {
            HaulJob haulJob = (HaulJob) job;
            Logger.getInstance().log(this,
                    "Haul job is finished, job removed - itemType: " + haulJob.getItem().getType());
            haulJob.getItem().setUsed(false);
            haulJobs.remove(haulJob);
        }
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Removing item - itemType: " + item.getType());
        boolean itemRemoved = false;
        itemRemoved = items.remove(item);
        if (itemRemoved) {
            MapIndex pos = item.getPosition().sub(area.pos);
            used[pos.x][pos.y] = false;
        }
        for (Item container : items) {
            if (container instanceof ContainerItem && ((ContainerItem) container).removeItem(item)) {
                itemRemoved = true;
                break;
            }
        }
        if (itemRemoved) {
            notifyListeners();
        }
        return itemRemoved;
    }

    /**
     * Sets the type of item that this stockpile will collect.
     * @param itemTypeName the item type name
     * @param accept true to accept the item type
     */
    public void setItemType(final String itemTypeName, final boolean accept) {
        Logger.getInstance().log(this, "Set item type - itemTypeName: " + itemTypeName + " accept: " + accept);
        ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
        if (accept) {
            if (!acceptableItemTypes.contains(itemType)) {
                acceptableItemTypes.add(itemType);
                player.getStockManager().addListener(itemType, this);
                createHaulJobs();
            }
        } else {
            acceptableItemTypes.remove(itemType);
            player.getStockManager().removeListener(itemType, this);
            // Interrupt and remove haul tasks that have been started
            for (HaulJob haulJob : haulJobs) {
                if (haulJob.getItem().getType().equals(itemTypeName)) {
                    haulJob.interrupt("The stockpile no longer accepts this item type");
                    MapIndex pos = haulJob.getDropPosition().sub(area.pos);
                    used[pos.x][pos.y] = false;
                }
            }
            // Remove items from this stockpile that are no longer accepted
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.getType().equals(itemType)) {
                    items.remove(i);
                    player.getStockManager().addItem(item);
                    MapIndex pos = item.getPosition().sub(area.pos);
                    used[pos.x][pos.y] = false;
                    i--;
                }
            }
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

    @Override
    public void stockManagerChanged() {
        createHaulJobs();
    }

    /**
     * Create haul tasks to fill up the stockpile.
     */
    private void createHaulJobs() {
        Logger.getInstance().log(this, "Create haul jobs");
        if (!acceptableItemTypes.isEmpty()) {
            for (int x = 0; x < area.width; x++) {
                for (int y = 0; y < area.height; y++) {
                    if (!used[x][y]) {
                        Item item = player.getStockManager().getUnstoredItem(acceptableItemTypes);
                        if (item != null) {
                            item.setUsed(true);
                            used[x][y] = true;
                            HaulJob haulJob = new HaulJob(item, this, area.pos.add(x, y, 0));
                            haulJob.addListener(this);
                            player.getJobManager().addJob(haulJob);
                            haulJobs.add(haulJob);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets an item from the stockpile that is at a specific map position.
     * @param index the map position
     * @return the item, null if no item found
     */
    public Item getItem(final MapIndex index) {
        for (Item item : items) {
            if (index.equals(item.getPosition())) {
                return item;
            }
        }
        return null;
    }

    /**
     * The stockpile has been deleted, clean up everything.
     */
    public void remove() {
        player.getStockManager().removeListener(this);
        for (Item item : items) {
            player.getStockManager().addItem(item);
        }
        for (HaulJob haulJob : haulJobs) {
            haulJob.interrupt("Stockpile deleted");
        }
    }
}
