package userinterface.game.guistate;

import simulation.map.MapArea;
import controller.command.CreateFarmCommand;

/**
 * The create farm GUI state.
 */
public class CreateFarmGuiState extends AbstractVariableSizeGuiState {

    @Override
    protected void doReleaseAction() {
        controller.addCommand(new CreateFarmCommand(new MapArea(absSelection), player));
    }

    @Override
    protected boolean checkAreaValid() {
        return true;
    }
}
