package yadf.ui.swing.game.guistate;

import yadf.controller.command.PlaceItemCommand;
import yadf.simulation.map.MapIndex;

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
    public String toString() {
        return "Place " + itemTypeName;
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
