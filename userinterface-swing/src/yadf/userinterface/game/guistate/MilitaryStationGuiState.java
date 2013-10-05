package yadf.userinterface.game.guistate;

import yadf.controller.command.MilitaryStationCommand;
import yadf.simulation.map.MapIndex;

/**
 * The military station GUI state.
 */
public class MilitaryStationGuiState extends AbstractFixedSizeGuiState {

    @Override
    protected void doClickAction() {
        controller.addCommand(new MilitaryStationCommand(player, new MapIndex(position)));
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

    @Override
    public String toString() {
        return "Military station";
    }
}
