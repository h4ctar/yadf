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

    void dropHaulItem(boolean b);

    void pickupHaulItem(Item item);

    void pickupTool(Item tool);

    Item getHaulItem();
}
