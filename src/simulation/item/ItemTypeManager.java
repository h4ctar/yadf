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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.IPlayer;
import simulation.Player;
import simulation.map.MapIndex;

/**
 * The Class ItemTypeManager.
 */
public final class ItemTypeManager {

    /** The instance. */
    private static ItemTypeManager instance;

    /** The item types. */
    private Set<ItemType> itemTypes = new LinkedHashSet<>();

    /** The item types, keyed by name. */
    private Map<String, ItemType> itemTypesByName = new HashMap<>();

    /** The item types, keyed by category. */
    private Map<String, Set<ItemType>> itemTypesByCategory = new HashMap<>();

    /** The embark items. */
    private Set<Item> embarkItems = new LinkedHashSet<>();

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
        return itemTypesByCategory.keySet();
    }

    /**
     * Returns a set of embark items.
     * @param player the player that the items need to belong to
     * @return the embark items
     */
    public List<Item> getEmbarkItems(final Player player) {
        List<Item> copyOfEmbarkItems = new ArrayList<>();
        for (Item item : embarkItems) {
            System.out.println(item.getType().name);
            Item copyItem = createItem(item, player);
            copyOfEmbarkItems.add(copyItem);
        }
        return copyOfEmbarkItems;
    }

    /**
     * Get all the item types.
     * @return all the item types
     */
    public Set<ItemType> getItemTypes() {
        return itemTypes;
    }

    /**
     * Gets the item type.
     * @param itemTypeName the item type name
     * @return the item type
     */
    public ItemType getItemType(final String itemTypeName) {
        ItemType itemType = itemTypesByName.get(itemTypeName);
        assert itemType != null;
        return itemType;
    }

    /**
     * Gets the item types from category.
     * @param category the category
     * @return the item types from category
     */
    public Set<ItemType> getItemTypesFromCategory(final String category) {
        return itemTypesByCategory.get(category);
    }

    /**
     * Gets the number of item types in category.
     * @param categoryName the category name
     * @return the number of item types in category
     */
    public int getNumberOfItemTypesInCategory(final String categoryName) {
        return itemTypesByCategory.get(categoryName).size();
    }

    /**
     * Gets the placeable items.
     * @return the placeable items
     */
    public Set<ItemType> getPlaceableItems() {
        Set<ItemType> placeableItemTypes = new LinkedHashSet<>();
        for (ItemType itemType : itemTypes) {
            if (itemType.placeable) {
                placeableItemTypes.add(itemType);
            }
        }
        return placeableItemTypes;
    }

    /**
     * Loads the item types and embark items.
     * @throws Exception the exception
     */
    public void load() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("item_types.xml");
        Document document = documentBuilder.parse(inputStream);
        Element root = document.getDocumentElement();
        loadItemTypes(root);

        inputStream = getClass().getClassLoader().getResourceAsStream("embark_items.xml");
        document = documentBuilder.parse(inputStream);
        root = document.getDocumentElement();
        loadEmbarkItems(root);
    }

    /**
     * Load all the item types from the item_types.xml.
     * @param root the element
     * @throws Exception something went wrong
     */
    public void loadItemTypes(final Element root) throws Exception {
        NodeList categoryNodes = root.getElementsByTagName("category");

        for (int i = 0; i < categoryNodes.getLength(); i++) {
            Node categoryeNode = categoryNodes.item(i);
            Element categoryElement = (Element) categoryeNode;
            String category = categoryElement.getAttribute("name");
            Set<ItemType> itemTypesInCategory = new LinkedHashSet<>();

            NodeList itemTypeNodes = categoryElement.getElementsByTagName("itemType");
            for (int j = 0; j < itemTypeNodes.getLength(); j++) {
                Node itemTypeNode = itemTypeNodes.item(j);
                Element itemTypeElement = (Element) itemTypeNode;
                ItemType itemType = new ItemType(itemTypeElement, category);

                itemTypesInCategory.add(itemType);
                itemTypes.add(itemType);
                itemTypesByName.put(itemType.name, itemType);
            }

            itemTypesByCategory.put(category, itemTypesInCategory);
        }
    }

    /**
     * Load all the embark item configuration from the embark_items.xml.
     * @param root the element
     * @throws Exception something went wrong
     */
    public void loadEmbarkItems(final Element root) throws Exception {
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
     * Unload all the item types.
     */
    public void unload() {
        itemTypes = new LinkedHashSet<>();
        itemTypesByName = new HashMap<>();
        itemTypesByCategory = new HashMap<>();
        embarkItems = new LinkedHashSet<>();
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
    public Item createItem(final MapIndex position, final ItemType itemType, final IPlayer player) {
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
    public Item createItem(final Item item, final Player player) {
        Item newItem;
        if (item.itemType.capacity > 0) {
            newItem = new ContainerItem((ContainerItem) item, player);
        } else {
            newItem = new Item(item, player);
        }
        return newItem;
    }
}
