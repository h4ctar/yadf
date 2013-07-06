package userinterface.game;

import userinterface.game.guistate.IGuiState;

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

    void setState(IGuiState state);
}
