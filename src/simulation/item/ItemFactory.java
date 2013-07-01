package simulation.item;

import org.w3c.dom.Element;

import simulation.IPlayer;
import simulation.map.MapIndex;

/**
 * Factory to create items.
 */
public final class ItemFactory {

    /**
     * Private constructor.
     */
    private ItemFactory() {

    }

    /**
     * Factory method to create the correct item.
     * @param itemElement The DOM element to get attributes from
     * @return the new item
     * @throws Exception something went wrong
     */
    public static Item createItem(final Element itemElement) throws Exception {
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
    public static Item createItem(final MapIndex position, final ItemType itemType, final IPlayer player) {
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
    public static Item createItem(final Item item, final IPlayer player) {
        Item newItem;
        if (item.itemType.capacity > 0) {
            newItem = new ContainerItem((ContainerItem) item, player);
        } else {
            newItem = new Item(item, player);
        }
        return newItem;
    }
}
