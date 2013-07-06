package userinterface.game.guistate;

import simulation.map.MapArea;
import controller.command.CreateRoomCommand;

public class CreateRoomGuiState extends AbstractVariableSizeGuiState {

    /** The room type. */
    private String roomType;

    public CreateRoomGuiState(String roomTypeTmp) {
        roomType = roomTypeTmp;
    }

    @Override
    protected void doReleaseAction() {
        controller.addCommand(new CreateRoomCommand(new MapArea(absSelection), player, roomType));
    }
}
