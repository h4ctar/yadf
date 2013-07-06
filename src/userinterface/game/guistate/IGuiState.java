package userinterface.game.guistate;

import simulation.IPlayer;
import userinterface.game.IGamePanel;
import controller.AbstractController;

/**
 * Interface for a GUI state.
 */
public interface IGuiState {

    /**
     * Setup the GUI state.
     * @param player the player
     * @param controller the controller
     * @param gamePanel the game panel
     */
    void setup(IPlayer player, AbstractController controller, IGamePanel gamePanel);

    /**
     * Add a listener.
     * @param listener the listener to add
     */
    void addGuiStateListener(IGuiStateListener listener);

    /**
     * Remove a listener.
     * @param listener the listener to remove
     */
    void removeGuiStateListener(IGuiStateListener listener);

    /**
     * Interrupt the GUI state.
     */
    void interrupt();
}
