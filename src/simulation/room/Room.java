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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simulation.AbstractGameObject;
import simulation.item.Item;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The Class Room.
 */
public class Room extends AbstractGameObject {

    /** The serial version UID. */
    private static final long serialVersionUID = -4517865861465305417L;

    /** The items. */
    private final List<Item> items = new ArrayList<>();

    /** The area. */
    private final MapArea area;

    /** The room type. */
    private final String roomType;

    /**
     * Instantiates a new room.
     * 
     * @param areaTmp the area
     * @param roomTypeTmp the room type
     */
    public Room(final MapArea areaTmp, final String roomTypeTmp) {
        area = areaTmp;
        roomType = roomTypeTmp;
    }

    /**
     * Adds the item.
     * 
     * @param item the item
     */
    public void addItem(final Item item) {
        items.add(item);
    }

    /**
     * Gets the area.
     * 
     * @return the area
     */
    public MapArea getArea() {
        return area;
    }

    /**
     * Gets the items.
     * 
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public MapIndex getPosition() {
        return new MapIndex(area.pos);
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return roomType;
    }

    /**
     * Checks for index.
     * 
     * @param index the index
     * @return true, if successful
     */
    public boolean hasIndex(final MapIndex index) {
        if (index.x >= area.pos.x && index.x <= area.pos.x + area.width && index.y >= area.pos.y
                && index.y <= area.pos.y + area.height && area.pos.z == index.z) {
            return true;
        }
        return false;
    }

    public Set<Item> getUnusedItems(String itemTypeName) {
        Set<Item> foundItems = new HashSet<>();
        for (Item item : items) {
            if (item.getType().equals(itemTypeName) && !item.isUsed()) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }
}
