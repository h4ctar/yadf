package userinterface.game.guistate;

import simulation.map.MapArea;
import controller.command.CreateRoomCommand;

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
}
