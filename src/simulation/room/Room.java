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
package simulation.room;

import java.util.LinkedHashSet;
import java.util.Set;

import logger.Logger;
import simulation.AbstractGameObject;
import simulation.IGameObjectManagerListener;
import simulation.IPlayer;
import simulation.item.ContainerComponent;
import simulation.item.IContainer;
import simulation.item.IItemAvailableListener;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class Room.
 */
public class Room extends AbstractGameObject implements IContainer {

    /** The container component; contains unstored items. */
    private final ContainerComponent containerComponent = new ContainerComponent(this);

    /** The area. */
    private final MapArea area;

    /** The room type. */
    private final String roomType;

    /** The player that this room belongs to. */
    private final IPlayer player;

    /**
     * Instantiates a new room.
     * @param areaTmp the area
     * @param roomTypeTmp the room type
     * @param playerTmp the player that this room belongs to
     */
    public Room(final MapArea areaTmp, final String roomTypeTmp, final IPlayer playerTmp) {
        area = areaTmp;
        roomType = roomTypeTmp;
        player = playerTmp;
    }

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Adding item: " + item.getType().name);
        boolean itemAdded = containerComponent.addItem(item);
        return itemAdded;
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Removing item: " + item.getType().name);
        return containerComponent.removeItem(item);
    }

    /**
     * Gets the area of the room.
     * @return the area
     */
    public MapArea getArea() {
        return area;
    }

    /**
     * Gets the items.
     * @return the items
     */
    @Override
    public Set<Item> getItems() {
        return containerComponent.getItems();
    }

    /**
     * Gets the position.
     * @return the position
     */
    public MapIndex getPosition() {
        return new MapIndex(area.pos);
    }

    /**
     * Gets the type.
     * @return the type
     */
    public String getType() {
        return roomType;
    }

    /**
     * Gets all the unused items of a specific type. This is used if the caller needs to make a decision about which
     * item to take, e.g. the closest item.
     * @param itemTypeName the type of item to get
     * @return all the found items
     */
    public Set<Item> getUnusedItems(final String itemTypeName) {
        Set<Item> foundItems = new LinkedHashSet<>();
        for (Item item : getItems()) {
            if (item.getType().name.equals(itemTypeName) && !item.isUsed()) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    @Override
    public Item getItem(final String itemTypeName, final boolean used, final boolean placed) {
        return containerComponent.getItem(itemTypeName, used, placed);
    }

    @Override
    public void delete() {
        super.delete();
        for (Item item : getItems()) {
            if (item.isPlaced()) {
                item.setPlaced(false);
            }
            player.getStockManager().addItem(item);
        }
    }

    @Override
    public Item getItemFromCategory(final String category, final boolean used, final boolean placed) {
        return containerComponent.getItemFromCategory(category, used, placed);
    }

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        containerComponent.addGameObjectManagerListener(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        containerComponent.removeGameObjectManagerListener(listener);
    }

    @Override
    public void addItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.addItemAvailableListener(itemType, listener);
    }

    @Override
    public void removeItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.removeItemAvailableListener(itemType, listener);
    }

    @Override
    public int getItemQuantity(final String category) {
        return containerComponent.getItemQuantity(category);
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        return containerComponent.getItemQuantity(itemType);
    }

    @Override
    public void addItemAvailableListenerListener(final String category, final IItemAvailableListener listener) {
        containerComponent.addItemAvailableListenerListener(category, listener);
    }

    @Override
    public void removeItemAvailableListener(final String category, final IItemAvailableListener listener) {
        containerComponent.removeItemAvailableListener(category, listener);
    }
}
