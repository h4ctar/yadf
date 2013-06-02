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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import logger.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.Player;
import simulation.map.MapIndex;

/**
 * The Class ItemTypeManager.
 */
public final class ItemTypeManager {

    /** The instance. */
    private static ItemTypeManager instance;

    /**
     * Gets the single instance of ItemTypeManager.
     * @return single instance of ItemTypeManager
     */
    public static ItemTypeManager getInstance() {
        if (instance == null) {
            instance = new ItemTypeManager();
        }
        return instance;
    }

    /** The item types. */
    private final Map<String, Map<String, ItemType>> itemTypes = new HashMap<>();

    /** The embark items. */
    private final List<Item> embarkItems = new ArrayList<>();

    /**
     * Instantiates a new item type manager.
     */
    private ItemTypeManager() {
    }

    /**
     * Gets the category names.
     * @return the category names
     */
    public Set<String> getCategoryNames() {
        return itemTypes.keySet();
    }

    /**
     * Returns a set of embark items.
     * @param player the player that the items need to belong to
     * @return the embark items
     */
    public List<Item> getEmbarkItems(final Player player) {
        // TODO: this needs to return clones of the list
        List<Item> copyOfEmbarkItems = new ArrayList<>();
        for (Item item : embarkItems) {
            Item copyItem = createItem(item, player);
            copyOfEmbarkItems.add(copyItem);
        }
        return copyOfEmbarkItems;
    }

    /**
     * Gets the item type.
     * @param itemTypeName the item type name
     * @return the item type
     */
    public ItemType getItemType(final String itemTypeName) {
        for (Map<String, ItemType> category : itemTypes.values()) {
            if (category.containsKey(itemTypeName)) {
                return category.get(itemTypeName);
            }
        }
        Logger.getInstance().log(null, "Item type does not exist: " + itemTypeName);
        return null;
    }

    /**
     * Gets the item types from category.
     * @param category the category
     * @return the item types from category
     */
    public Collection<ItemType> getItemTypesFromCategory(final String category) {
        return itemTypes.get(category).values();
    }

    /**
     * Gets the number of item types in category.
     * @param categoryName the category name
     * @return the number of item types in category
     */
    public int getNumberOfItemTypesInCategory(final String categoryName) {
        return itemTypes.get(categoryName).size();
    }

    /**
     * Gets the placeable items.
     * @return the placeable items
     */
    public Set<ItemType> getPlaceableItems() {
        Set<ItemType> placeableItemTypes = new HashSet<>();
        for (Map<String, ItemType> items : itemTypes.values()) {
            for (ItemType itemType : items.values()) {
                if (itemType.placeable) {
                    placeableItemTypes.add(itemType);
                }
            }
        }
        return placeableItemTypes;
    }

    /**
     * Load.
     * @throws Exception the exception
     */
    public void load() throws Exception {
        loadItemTypes();
        loadEmbarkItems();
    }

    /**
     * Load all the embark item configuration from the embark_items.xml.
     * @throws Exception something went wrong
     */
    private void loadEmbarkItems() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("embark_items.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        Element root = document.getDocumentElement();
        NodeList itemNodes = root.getElementsByTagName("item");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Node itemNode = itemNodes.item(i);
            Element itemElement = (Element) itemNode;
            String tempString = itemElement.getAttribute("quantity");
            int quantity = "".equals(tempString) ? 1 : Integer.parseInt(tempString);
            for (int j = 0; j < quantity; j++) {
                Item item = createItem(itemElement);
                embarkItems.add(item);
            }
        }
    }

    /**
     * Load all the item types from the item_types.xml.
     * @throws Exception something went wrong
     */
    private void loadItemTypes() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("item_types.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        NodeList categoryNodes = document.getElementsByTagName("category");
        for (int i = 0; i < categoryNodes.getLength(); i++) {
            Node categoryeNode = categoryNodes.item(i);
            Element categoryElement = (Element) categoryeNode;
            String category = categoryElement.getAttribute("name");
            Map<String, ItemType> categories = new HashMap<>();
            itemTypes.put(category, categories);
            NodeList itemTypeNodes = categoryElement.getElementsByTagName("itemType");
            for (int j = 0; j < itemTypeNodes.getLength(); j++) {
                Node itemTypeNode = itemTypeNodes.item(j);
                Element itemTypeElement = (Element) itemTypeNode;
                ItemType itemType = new ItemType(itemTypeElement, category);
                categories.put(itemType.name, itemType);
            }
        }
    }

    /**
     * Factory method to create the correct item.
     * @param itemElement The DOM element to get attributes from
     * @return the new item
     * @throws Exception something went wrong
     */
    public Item createItem(final Element itemElement) throws Exception {
        Item item;
        String itemTypeName = itemElement.getAttribute("type");
        ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
        if (itemType.capacity > 0) {
            item = new ContainerItem(itemElement);
        } else {
            item = new Item(itemElement);
        }
        return item;
    }

    /**
     * Create an item from an item type.
     * @param position the position of the new item
     * @param itemType the type of the new item
     * @param player the player that the new item will belong to
     * @return the new item
     */
    public Item createItem(final MapIndex position, final ItemType itemType, final Player player) {
        Item item;
        if (itemType.capacity > 0) {
            item = new ContainerItem(position, itemType, player);
        } else {
            item = new Item(position, itemType, player);
        }
        return item;
    }

    /**
     * Create an item from another item.
     * @param item the item to clone
     * @param player the player that the new item will belong to
     * @return the new item
     */
    private Item createItem(final Item item, final Player player) {
        Item newItem;
        if (item.itemType.capacity > 0) {
            newItem = new ContainerItem((ContainerItem) item, player);
        } else {
            newItem = new Item(item, player);
        }
        return newItem;
    }
}