package yadf.ui.swing.game.guistate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.swing.game.IGamePanel;

/**
 * An abstract GUI state for a command that is fixed in size.
 */
public abstract class AbstractFixedSizeGuiState extends AbstractGuiState implements MouseListener,
        MouseMotionListener {

    /** The position. */
    protected MapIndex position;

    /** Is the current selection valid. */
    private boolean areaValid;

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp, final IGamePanel gamePanel) {
        super.setup(playerTmp, controllerTmp, gamePanel);
        gamePanel.getWorldPanel().addMouseListener(this);
        gamePanel.getWorldPanel().addMouseMotionListener(this);
    }

    @Override
    public void interrupt() {
        cleanStuff();
    }

    /**
     * Clean everything.
     */
    private void cleanStuff() {
        gamePanel.getWorldPanel().removeMouseListener(this);
        gamePanel.getWorldPanel().removeMouseMotionListener(this);
        gamePanel.getWorldPanel().setSelection(null, false);
        notifyListeners();
    }

    /**
     * Do the actual command or whatever.
     */
    protected abstract void doClickAction();

    /**
     * Get the width of the area.
     * @return the width
     */
    protected abstract int getWidth();

    /**
     * Get the height of the area.
     * @return the height
     */
    protected abstract int getHeight();

    /**
     * Should the area be checked if its valid, to change the colour of the selection.
     * @return true if the area should be checked
     */
    protected abstract boolean checkAreaValid();

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (areaValid) {
            doClickAction();
        }
        if (e.isShiftDown()) {
            gamePanel.getWorldPanel().setSelection(null, false);
        } else {
            cleanStuff();
        }
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
        position = gamePanel.getWorldPanel().getMouseIndex(e.getX(), e.getY());
        MapArea selection = new MapArea(position, getWidth(), getHeight());
        areaValid = gamePanel.getWorldPanel().setSelection(selection, checkAreaValid());
    }
}
