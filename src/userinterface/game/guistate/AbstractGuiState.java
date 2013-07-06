package userinterface.game.guistate;

import java.util.HashSet;
import java.util.Set;

import simulation.IPlayer;
import userinterface.game.ManagementPanel;
import userinterface.game.WorldCanvas;
import controller.AbstractController;

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
    protected WorldCanvas worldPanel;

    /** The management panel. */
    protected ManagementPanel managementPanel;

    /** The listeners. */
    private Set<IGuiStateListener> listeners = new HashSet<>();

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp,
            final WorldCanvas worldPanelTmp, final ManagementPanel managementPanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        worldPanel = worldPanelTmp;
        managementPanel = managementPanelTmp;
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
