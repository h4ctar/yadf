package simulation.character;

import simulation.character.component.ICharacterComponent;
import simulation.item.Item;

public interface IInventoryComponent extends ICharacterComponent {

    Item getToolHolding();

    void dropTool();

    void dropHaulItem(boolean b);

    void pickupHaulItem(Item item);

    void pickupTool(Item tool);

    Item getHaulItem();
}
