package yadf.ui.swing.game.guistate;

import yadf.controller.command.CreateStockpileCommand;
import yadf.simulation.map.MapArea;

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

    @Override
    public String toString() {
        return "Create stockpile";
    }
}
