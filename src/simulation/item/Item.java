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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.AbstractEntity;
import simulation.map.MapIndex;

/**
 * The Class Item.
 */
public class Item extends AbstractEntity {

    /** The type of the item. */
    private final ItemType itemType;

    /** Is the item being used by a dwarf. */
    private boolean used = false;

    /** Is the item placed, i.e. a table, bed or door */
    private boolean placed;

    /** The contents of this item if it's a container. */
    private final List<Item> contents = new ArrayList<>();

    /** If the item is stored in a barrel or stockpile. */
    private boolean stored;

    /**
     * Create an item from a DOM element.
     * @param itemElement the DOM element
     * @throws Exception something went wrong
     */
    public Item(final Element itemElement) throws Exception {
        super(new MapIndex());
        String itemTypeName = itemElement.getAttribute("type");
        itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
        NodeList childNodes = itemElement.getChildNodes();
        for (int k = 0; k < childNodes.getLength(); k++) {
            Node itemsNode = childNodes.item(k);
            if (itemsNode != null && itemsNode instanceof Element && itemsNode.getNodeName().equals("items")) {
                Element itemsElement = (Element) itemsNode;
                NodeList itemNodes = itemsElement.getChildNodes();
                if (itemNodes.getLength() > itemType.capacity) {
                    throw new Exception(itemType.name + " can't hold more than " + itemType.capacity
                            + " you're trying to put " + itemNodes.getLength() + " contents into it!");
                }
                for (int i = 0; i < itemNodes.getLength(); i++) {
                    Node contentItemNode = itemNodes.item(i);
                    if (contentItemNode instanceof Element) {
                        Element contentItemElement = (Element) contentItemNode;
                        String tempString = contentItemElement.getAttribute("quantity");
                        int quantity = "".equals(tempString) ? 1 : Integer.parseInt(tempString);
                        for (int j = 0; j < quantity; j++) {
                            Item contentItem = new Item(contentItemElement);
                            contentItem.setStored(true);
                            contents.add(contentItem);
                        }
                    }
                }
            }
        }
    }

    /**
     * The constructor for the item.
     * @param position The initial position of the item
     * @param itemTypeTmp the item type name
     */
    public Item(final MapIndex position, final ItemType itemTypeTmp) {
        super(position);
        itemType = itemTypeTmp;
    }

    /**
     * The constructor for the item.
     * @param position The initial position of the item
     * @param itemTypeName the item type name
     */
    public Item(final MapIndex position, final String itemTypeName) {
        super(position);
        itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
    }

    /**
     * Gets the type of the item.
     * @return The type of the item
     */
    public ItemType getType() {
        return itemType;
    }

    /**
     * Returns an unused item if it exists in this items contents.
     * @param itemTypeName the type of the item that you want
     * @return the unused item
     */
    public Item getUnusedItem(final String itemTypeName) {
        for (Item contentItem : contents) {
            if (contentItem.getType().equals(itemTypeName) && !contentItem.isUsed() && !contentItem.getRemove()
                    && !contentItem.isPlaced()) {
                return contentItem;
            }
        }
        return null;
    }

    /**
     * Returns an unused item if it exists in this items contents.
     * @param category the category of the item that you want
     * @return the unused item
     */
    public Item getUnusedItemFromCategory(final String category) {
        for (Item contentItem : contents) {
            if (contentItem.getType().category.equals(category) && !contentItem.isUsed() && !contentItem.getRemove()
                    && !contentItem.isPlaced()) {
                return contentItem;
            }
        }
        return null;
    }

    /**
     * Checks if is placed.
     * @return true, if is placed
     */
    public boolean isPlaced() {
        return placed;
    }

    /**
     * Gets if the item is stored in either a stockpile or a barrel.
     * @return true if its stored
     */
    public boolean isStored() {
        return stored;
    }

    /**
     * Is the item being used.
     * @return Boolean representing if it is used or not
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Remove the item from the container.
     * @param item the item to remove
     * @return true if the item was removed
     */
    public boolean removeItemFromContainer(final Item item) {
        return contents.remove(item);
    }

    /**
     * Sets the placed.
     * @param placedTmp the new placed
     */
    public void setPlaced(final boolean placedTmp) {
        placed = placedTmp;
    }

    @Override
    public void setPosition(final MapIndex positionTmp) {
        super.setPosition(positionTmp);
        for (Item item : contents) {
            item.setPosition(positionTmp);
        }
    }

    /**
     * Set if the item is stored or not.
     * @param storedTmp true if the item should be set to stored
     */
    public void setStored(final boolean storedTmp) {
        stored = storedTmp;
    }

    /**
     * Sets the item as being used, this is to make sure no one will try claim it.
     * @param usedTmp Boolean representing if it is used or not
     */
    public void setUsed(final boolean usedTmp) {
        used = usedTmp;
    }

    @Override
    public String toString() {
        return itemType.toString();
    }
}
