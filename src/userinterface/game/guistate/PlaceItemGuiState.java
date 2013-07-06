package userinterface.game.guistate;

import simulation.map.MapIndex;
import controller.command.PlaceItemCommand;

/**
 * The place item GUI state.
 */
public class PlaceItemGuiState extends AbstractFixedSizeGuiState {

    /** The type of item to place. */
    private String itemTypeName;

    /**
     * Constructor.
     * @param itemTypeNameTmp the type of item to place
     */
    public PlaceItemGuiState(final String itemTypeNameTmp) {
        itemTypeName = itemTypeNameTmp;
    }

    @Override
    protected void doClickAction() {
        controller.addCommand(new PlaceItemCommand(player, itemTypeName, new MapIndex(position)));
    }

    @Override
    protected int getWidth() {
        return 1;
    }

    @Override
    protected int getHeight() {
        return 1;
    }

    @Override
    protected boolean checkAreaValid() {
        return true;
    }
}
