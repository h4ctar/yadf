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

import java.util.HashSet;
import java.util.Set;

import logger.Logger;
import simulation.AbstractGameObject;
import simulation.IPlayer;
import simulation.item.IContainer;
import simulation.item.Item;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class Room.
 */
public class Room extends AbstractGameObject implements IContainer {

    /** The serial version UID. */
    private static final long serialVersionUID = -4517865861465305417L;

    /** The items. */
    private final Set<Item> items = new HashSet<>();

    /** The area. */
    private final MapArea area;

    /** The room type. */
    private final String roomType;

    /** The player that this room belongs to. */
    private final IPlayer player;

    /** The listeners. */
    private final Set<IRoomListener> listeners = new HashSet<>();

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

    /**
     * Adds an item to this room.
     * @param item the item
     * @return true if the item was successfully added
     */
    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Item added: " + item.getType().name);
        items.add(item);
        for (IRoomListener listener : listeners) {
            listener.itemAdded(item);
        }
        return true;
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
        return items;
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
     * Checks for index.
     * @param index the index
     * @return true, if successful
     */
    public boolean hasIndex(final MapIndex index) {
        if (index.x >= area.pos.x && index.x <= area.pos.x + area.width - 1 && index.y >= area.pos.y
                && index.y <= area.pos.y + area.height - 1 && area.pos.z == index.z) {
            return true;
        }
        return false;
    }

    /**
     * Gets all the unused items of a specific type.
     * @param itemTypeName the type of item to get
     * @return all the found items
     */
    public Set<Item> getUnusedItems(final String itemTypeName) {
        Set<Item> foundItems = new HashSet<>();
        for (Item item : items) {
            if (item.getType().name.equals(itemTypeName) && !item.isUsed()) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    @Override
    public Item getUnusedItem(final String itemTypeName) {
        for (Item item : items) {
            if (item.getType().name.equals(itemTypeName) && !item.isUsed()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void delete() {
        super.delete();
        player.removeRoom(this);
        for (Item item : items) {
            if (item.isPlaced()) {
                item.setPlaced(false);
            }
            player.getStockManager().addItem(item);
        }
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Item removed: " + item.getType().name);
        boolean removed = items.remove(item);
        if (removed) {
            for (IRoomListener listener : listeners) {
                listener.itemRemoved(item);
            }
        }
        return removed;
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        // TODO Auto-generated method stub
        assert false;
        return null;
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
}
