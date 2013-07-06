package userinterface.game.guistate;

import simulation.map.MapIndex;
import controller.command.MilitaryStationCommand;

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
