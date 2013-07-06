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
 * An abstract GUI state that has the functionality required to perform a command on a selection.
 */
public abstract class AbstractVariableSizeGuiState extends AbstractGuiState implements MouseListener,
        MouseMotionListener {

    /** The selection. */
    private MapArea selection = new MapArea();

    /** The absolute selection. */
    protected MapArea absSelection = new MapArea();

    /** The mouse button. */
    protected int button;

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp,
            final WorldCanvas worldPanelTmp, final ManagementPanel managementPanelTmp) {
        super.setup(playerTmp, controllerTmp, worldPanelTmp, managementPanelTmp);
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

    /**
     * Do the action for the mouse release.
     */
    protected abstract void doReleaseAction();

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        button = e.getButton();
        MapIndex mouseIndex = worldPanel.getMouseIndex(e.getX(), e.getY());
        selection.pos = mouseIndex;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        doReleaseAction();
        cleanStuff();
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        MapIndex mouseIndex = worldPanel.getMouseIndex(e.getX(), e.getY());
        selection.width = mouseIndex.x - selection.pos.x + 1;
        selection.height = mouseIndex.y - selection.pos.y + 1;
        absSelection = new MapArea(selection);
        if (selection.width < 1) {
            absSelection.pos.x = selection.pos.x + selection.width - 1;
            absSelection.width = -selection.width + 2;
        }
        if (selection.height < 1) {
            absSelection.pos.y = selection.pos.y + selection.height - 1;
            absSelection.height = -selection.height + 2;
        }
        worldPanel.setSelection(absSelection, true);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }
}
