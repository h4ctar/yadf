package yadf.ui.swing.game;

import yadf.ui.swing.game.guistate.IGuiState;

/**
 * Interface for the Game Panel.
 */
public interface IGamePanel {

    /**
     * Update the game panel.
     */
    void update();

    /**
     * Disconnect the game panel.
     */
    void disconnect();

    /**
     * Set the GUI state.
     * @param state the new state
     */
    void setState(IGuiState state);

    /**
     * Get the world panel.
     * @return the world panel
     */
    WorldPanel getWorldPanel();

    /**
     * Get the management panel.
     * @return the management panel
     */
    ManagementPanel getManagementPanel();
}
