package userinterface.game.guistate;

import simulation.IPlayer;
import userinterface.game.ManagementPanel;
import userinterface.game.WorldCanvas;
import controller.AbstractController;

public interface IGuiState {
    void setup(IPlayer player, AbstractController controller, WorldCanvas worldPanel, ManagementPanel managementPanel);

    void addGuiStateListener(IGuiStateListener listener);

    void removeGuiStateListener(IGuiStateListener listener);

    void interrupt();
}
