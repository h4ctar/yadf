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

import yadf.simulation.IEntity;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * The Class Stockpile.
 */
public class Stockpile extends ItemManager implements IItemManager, IEntity {

    public Stockpile(MapArea area, IPlayer player) {
    }

    @Override
    public MapIndex getPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPosition(MapIndex positionTmp) {
        // TODO Auto-generated method stub

    }

    @Override
    public MapArea getArea() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasIndex(MapIndex index) {
        // TODO Auto-generated method stub
        return false;
    }
}

// /** Is the position in the stockpile used. */
// private final boolean[][] used;
//
// /** What item type the stockpile accepts. */
// private final Set<ItemType> acceptableItemTypes = new LinkedHashSet<>();
//
// /** The player that this stockpile belongs to. */
// private final IPlayer player;
//
// /**
// * An array of haul tasks that have been created for this stockpile, its remembered so they can be canceled if the
// * stockpile is deleted or its items to collect changes.
// */
// private final Set<HaulJob> haulJobs = new LinkedHashSet<>();
//
// /**
// * Constructor for the stockpile.
// * @param area The area the stockpile will occupy
// * @param playerTmp the player the stockpile belongs to
// */
// public Stockpile(final MapArea area, final IPlayer playerTmp) {
// super(area);
// player = playerTmp;
// used = new boolean[area.width][area.height];
// Logger.getInstance().log(this, "New stockpile - id: " + getId());
//
// for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
// setItemType(itemType.name, true);
// }
// }
//
// @Override
// public String toString() {
// return "Stockpile";
// }
//
// @Override
// public boolean addItem(final Item item) {
// Logger.getInstance().log(this, "Adding item - itemType: " + item.getType());
// boolean itemAdded = false;
// if (isDeleted()) {
// Logger.getInstance().log(this, "Stockpile has been deleted", true);
// } else {
// if (item.canBeStored(acceptableItemTypes)) {
// MapIndex pos = item.getPosition().sub(getPosition());
// itemAdded = containerComponent.addItem(item);
// used[pos.x][pos.y] = itemAdded;
// } else {
// Logger.getInstance().log(this, "Stockpile does not accept this item type", true);
// }
// }
// return itemAdded;
// }
//
// @Override
// public boolean removeItem(final Item item) {
// Logger.getInstance().log(this, "Removing item - itemType: " + item.getType());
// boolean itemRemoved = false;
// itemRemoved = containerComponent.removeItem(item);
// if (itemRemoved) {
// MapIndex pos = item.getPosition().sub(getPosition());
// if (getItem(item.getPosition()) == null) {
// used[pos.x][pos.y] = false;
// }
// }
// return itemRemoved;
// }
//
// /**
// * Get if the stockpile accepts a certain item type.
// * @param itemTypeName the item type name
// * @return the accepts item type
// */
// public boolean isItemTypeAccepted(final String itemTypeName) {
// boolean accepted = false;
// for (ItemType itemType : acceptableItemTypes) {
// if (itemType.name.equals(itemTypeName)) {
// accepted = true;
// break;
// }
// }
// return accepted;
// }
//
// /**
// * Gets all the stored items.
// * @return the items
// */
// @Override
// public List<Item> getItems() {
// return containerComponent.getItems();
// }
//
// /**
// * Returns the types of item that this stockpile will accept.
// * @return the types of item that this stockpile will accept
// */
// public Set<ItemType> getItemTypes() {
// return acceptableItemTypes;
// }
//
// @Override
// public Item getItem(final String itemTypeName, final boolean usedTmp, final boolean placed) {
// return containerComponent.getItem(itemTypeName, usedTmp, placed);
// }
//
// @Override
// public Item getItemFromCategory(final String category, final boolean usedTmp, final boolean placed) {
// return containerComponent.getItemFromCategory(category, usedTmp, placed);
// }
//
// /**
// * Checks for all item types in category.
// * @param category the category
// * @return true if it contains at least one of each item in the category
// */
// public boolean hasAllItemTypesInCategory(final String category) {
// int count = 0;
// for (ItemType itemType : acceptableItemTypes) {
// if (itemType.category.equals(category)) {
// count++;
// }
// }
// return count == ItemTypeManager.getInstance().getNumberOfItemTypesInCategory(category);
// }
//
// @Override
// public void jobDone(final IJob job) {
// assert job.isDone();
// HaulJob haulJob = (HaulJob) job;
// Logger.getInstance()
// .log(this, "Haul job is finished, job removed - itemType: " + haulJob.getItem().getType());
// haulJob.getItem().setAvailable(true);
// haulJobs.remove(haulJob);
// }
//
// @Override
// public void jobChanged(final IJob job) {
// // do nothing
// }
//
// /**
// * Sets the type of item that this stockpile will collect.
// * @param itemTypeName the item type name
// * @param accept true to accept the item type
// */
// public void setItemType(final String itemTypeName, final boolean accept) {
// Logger.getInstance().log(this, "Set item type - itemTypeName: " + itemTypeName + " accept: " + accept);
// ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
// if (accept) {
// if (!acceptableItemTypes.contains(itemType)) {
// acceptableItemTypes.add(itemType);
// player.getComponent(IStockManager.class).addItemAvailableListener(itemType, this);
// createHaulJobs();
// }
// } else {
// acceptableItemTypes.remove(itemType);
// player.getComponent(IStockManager.class).removeItemAvailableListener(itemType, this);
// // Interrupt and remove haul tasks that have been started
// for (HaulJob haulJob : haulJobs) {
// if (haulJob.getItem().getType().name.equals(itemTypeName)) {
// haulJob.interrupt("The stockpile no longer accepts this item type");
// MapIndex pos = haulJob.getPosition().sub(getPosition());
// used[pos.x][pos.y] = false;
// }
// }
// // Remove items from this stockpile that are no longer accepted
// for (Item item : getItems().toArray(new Item[0])) {
// if (item.getType().equals(itemType)) {
// getItems().remove(item);
// player.getComponent(IStockManager.class).addItem(item);
// MapIndex pos = item.getPosition().sub(getPosition());
// used[pos.x][pos.y] = false;
// }
// }
// }
// }
//
// /**
// * Create haul tasks to fill up the stockpile.
// */
// private void createHaulJobs() {
// Logger.getInstance().log(this, "Create haul jobs");
// if (!acceptableItemTypes.isEmpty()) {
// for (int x = 0; x < getWidth(); x++) {
// for (int y = 0; y < getHeight(); y++) {
// if (!used[x][y]) {
// Item item = player.getComponent(IStockManager.class).getUnstoredItem(acceptableItemTypes);
// if (item != null) {
// item.setAvailable(false);
// used[x][y] = true;
// HaulJob haulJob = new HaulJob(item, this, getPosition().add(x, y, 0), player);
// haulJob.addListener(this);
// player.getComponent(IJobManager.class).addJob(haulJob);
// haulJobs.add(haulJob);
// }
// }
// }
// }
// }
// }
//
// /**
// * Gets an item from the stockpile that is at a specific map position.
// * @param index the map position
// * @return the item, null if no item found
// */
// public Item getItem(final MapIndex index) {
// for (Item item : getItems()) {
// if (index.equals(item.getPosition())) {
// return item;
// }
// }
// return null;
// }
//
// /**
// * The stockpile has been deleted, clean up everything.
// */
// @Override
// public void delete() {
// super.delete();
// containerComponent.delete();
// for (ItemType itemType : acceptableItemTypes) {
// player.getComponent(IStockManager.class).removeItemAvailableListener(itemType, this);
// }
// for (Item item : getItems()) {
// player.getComponent(IStockManager.class).addItem(item);
// }
// for (HaulJob haulJob : haulJobs) {
// haulJob.interrupt("Stockpile deleted");
// }
// }
// }
