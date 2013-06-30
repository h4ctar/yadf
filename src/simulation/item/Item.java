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

import org.w3c.dom.Element;

import simulation.AbstractEntity;
import simulation.IPlayer;
import simulation.Player;
import simulation.map.MapIndex;

/**
 * The Class Item.
 */
public class Item extends AbstractEntity {

    /** The type of the item. */
    protected final ItemType itemType;

    /** Is the item being used by a dwarf. */
    protected boolean used = false;

    /** Is the item placed, i.e. a table, bed or door */
    protected boolean placed = false;

    /** The player that this item belongs to. */
    protected final IPlayer player;

    /** Item available listeners. */
    private final Set<IItemAvailableListener> listeners = new LinkedHashSet<>();

    /**
     * Create an item from a DOM element.
     * @param itemElement the DOM element
     * @throws Exception something went wrong
     */
    public Item(final Element itemElement) throws Exception {
        super(new MapIndex());
        player = null;
        String itemTypeName = itemElement.getAttribute("type");
        itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
    }

    /**
     * The constructor for the item.
     * @param position The initial position of the item
     * @param itemTypeTmp the item type name
     * @param player2 the player that this item belongs to
     */
    public Item(final MapIndex position, final ItemType itemTypeTmp, final IPlayer player2) {
        super(position);
        itemType = itemTypeTmp;
        player = player2;
    }

    /**
     * Copy constructor.
     * @param item the item to clone
     * @param playerTmp if null, will use original items player otherwise it will use the passed player
     */
    public Item(final Item item, final Player playerTmp) {
        super(item.position);
        itemType = item.getType();
        if (playerTmp == null) {
            player = item.player;
        } else {
            player = playerTmp;
        }
    }

    /**
     * Gets the type of the item.
     * @return The type of the item
     */
    public ItemType getType() {
        return itemType;
    }

    /**
     * Checks if is placed.
     * @return true, if is placed
     */
    public boolean isPlaced() {
        return placed;
    }

    /**
     * Is the item being used.
     * @return Boolean representing if it is used or not
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Sets the placed.
     * @param placedTmp the new placed
     */
    public void setPlaced(final boolean placedTmp) {
        placed = placedTmp;
    }

    /**
     * Sets the item as being used, this is to make sure no one will try claim it.
     * @param usedTmp Boolean representing if it is used or not
     */
    public void setUsed(final boolean usedTmp) {
        used = usedTmp;
        if (!used) {
            for (IItemAvailableListener listener : listeners) {
                listener.itemAvailable(this, null);
            }
        }
    }

    @Override
    public String toString() {
        return itemType.toString();
    }

    /**
     * Can this item be stored in a container that accepts certain item types.
     * @param itemTypes the item types that this item must match
     * @return true if it can be stored
     */
    public boolean canBeStored(final Set<ItemType> itemTypes) {
        boolean storable = false;
        if (itemTypes.contains(itemType) && !isDeleted() && !placed) {
            storable = true;
        }
        return storable;
    }

    /**
     * Add a new listener.
     * @param listener the new listener
     */
    public void addListener(final IItemAvailableListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    /**
     * Remove a listener.
     * @param listener the listener to remove
     */
    public void removeListener(final IItemAvailableListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }
}
