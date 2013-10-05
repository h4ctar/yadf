package yadf.simulation.character.component;

import yadf.simulation.item.Item;

/**
 * Interface for an inventory component.
 */
public interface IInventoryComponent extends ICharacterComponent {

    /**
     * Get the tool that the character is holding.
     * @return the tool
     */
    Item getToolHolding();

    /**
     * Drop the tool that the character is holding.
     */
    void dropTool();

    /**
     * Drop the item that the dwarf is hauling.
     * @param freeItem if true the item will be set to not used
     */
    void dropHaulItem(boolean freeItem);

    /**
     * Pickup a new haul item.
     * @param item the item to pickup
     */
    void pickupHaulItem(Item item);

    /**
     * Pickup a tool.
     * @param tool the tool to pickup
     */
    void pickupTool(Item tool);

    /**
     * Get the current haul item.
     * @return the haul item
     */
    Item getHaulItem();
}
