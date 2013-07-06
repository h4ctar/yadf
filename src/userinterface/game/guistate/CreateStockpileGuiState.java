package userinterface.game.guistate;

import simulation.map.MapArea;
import controller.command.CreateStockpileCommand;

/**
 * The create stockpile GUI state.
 */
public class CreateStockpileGuiState extends AbstractVariableSizeGuiState {

    @Override
    protected void doReleaseAction() {
        controller.addCommand(new CreateStockpileCommand(new MapArea(absSelection), player));
    }

    @Override
    protected boolean checkAreaValid() {
        return true;
    }
}
