package yadf.userinterface.game.guistate;

import yadf.controller.command.CreateFarmCommand;
import yadf.simulation.map.MapArea;

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

    @Override
    public String toString() {
        return "Create farm";
    }
}
