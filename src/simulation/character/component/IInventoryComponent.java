package simulation.character.component;

import simulation.item.Item;

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

    void pickupHaulItem(Item item);

    void pickupTool(Item tool);

    Item getHaulItem();
}
