package userinterface.game.guistate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import simulation.IPlayer;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import userinterface.game.ManagementPanel;
import userinterface.game.WorldCanvas;
import controller.AbstractController;

/**
 * An abstract GUI state for a command that is fixed in size.
 */
public abstract class AbstractFixedSizeGuiState extends AbstractGuiState implements MouseListener,
        MouseMotionListener {

    /** The player. */
    protected IPlayer player;

    protected AbstractController controller;

    protected WorldCanvas worldPanel;

    protected MapIndex position;

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp,
            final WorldCanvas worldPanelTmp, final ManagementPanel managementPanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        worldPanel = worldPanelTmp;

        worldPanel.addMouseListener(this);
        worldPanel.addMouseMotionListener(this);
    }

    @Override
    public void interrupt() {
        cleanStuff();
    }

    /**
     * Clean everything.
     */
    private void cleanStuff() {
        worldPanel.removeMouseListener(this);
        worldPanel.removeMouseMotionListener(this);
        worldPanel.setSelection(null, true);
        notifyListeners();
    }

    protected abstract void doClickAction();

    protected abstract int getWidth();

    protected abstract int getHeight();

    @Override
    public void mouseClicked(final MouseEvent e) {
        doClickAction();
        cleanStuff();
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        position = worldPanel.getMouseIndex(e.getX(), e.getY());
        MapArea selection = new MapArea(position, getWidth(), getHeight());
        worldPanel.setSelection(selection, true);
    }
}
