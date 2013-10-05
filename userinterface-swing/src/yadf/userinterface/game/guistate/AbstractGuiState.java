package yadf.userinterface.game.guistate;

import java.util.HashSet;
import java.util.Set;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.userinterface.game.IGamePanel;

/**
 * An abstract GUI state.
 * <p>
 * Implements the listener stuffs.
 */
public abstract class AbstractGuiState implements IGuiState {

    /** The player. */
    protected IPlayer player;

    /** The controller. */
    protected AbstractController controller;

    /** The world panel. */
    protected IGamePanel gamePanel;

    /** The listeners. */
    private Set<IGuiStateListener> listeners = new HashSet<>();

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp, final IGamePanel gamePanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        gamePanel = gamePanelTmp;
    }

    @Override
    public void addGuiStateListener(final IGuiStateListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGuiStateListener(final IGuiStateListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that the state is done.
     */
    protected void notifyListeners() {
        for (IGuiStateListener listener : listeners) {
            listener.stateDone();
        }
    }
}
