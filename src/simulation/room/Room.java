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

    /** The listeners. */
    private final Set<IRoomListener> listeners = new LinkedHashSet<>();

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
        if (itemAdded) {
            for (IRoomListener listener : listeners) {
                listener.itemAdded(item);
            }
        }
        return itemAdded;
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Removing item: " + item.getType().name);
        boolean itemRemoved = containerComponent.removeItem(item);
        if (itemRemoved) {
            for (IRoomListener listener : listeners) {
                listener.itemRemoved(item);
            }
        }
        return itemRemoved;
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
    public Item getUnusedItem(final String itemTypeName) {
        return containerComponent.getUnusedItem(itemTypeName);
    }

    @Override
    public void delete() {
        super.delete();
        player.removeRoom(this);
        for (Item item : getItems()) {
            if (item.isPlaced()) {
                item.setPlaced(false);
            }
            player.getStockManager().addItem(item);
        }
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        return containerComponent.getUnusedItemFromCategory(category);
    }

    /**
     * Add a listener to this room that will be notified whenever an item is added or removed.
     * @param listener the listener to add
     */
    public void addListener(final IRoomListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener from this room.
     * @param listener the listener to remove
     */
    public void removeListener(final IRoomListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.addListener(itemType, listener);
    }

    @Override
    public void removeListener(final ItemType itemType, final IItemAvailableListener listener) {
        containerComponent.removeListener(itemType, listener);
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
    public void addListener(final String category, final IItemAvailableListener listener) {
        containerComponent.addListener(category, listener);
    }

    @Override
    public void removeListener(final String category, final IItemAvailableListener listener) {
        containerComponent.removeListener(category, listener);
    }
}
