package yadf.userinterface.game.guistate;

import yadf.controller.command.CreateRoomCommand;
import yadf.simulation.map.MapArea;

/**
 * GUI state to create a room.
 */
public class CreateRoomGuiState extends AbstractVariableSizeGuiState {

    /** The room type. */
    private String roomType;

    /**
     * Constructor.
     * @param roomTypeTmp the type of room to create
     */
    public CreateRoomGuiState(final String roomTypeTmp) {
        roomType = roomTypeTmp;
    }

    @Override
    protected void doReleaseAction() {
        controller.addCommand(new CreateRoomCommand(new MapArea(absSelection), player, roomType));
    }

    @Override
    protected boolean checkAreaValid() {
        return true;
    }

    @Override
    public String toString() {
        return "Create " + roomType;
    }
}
