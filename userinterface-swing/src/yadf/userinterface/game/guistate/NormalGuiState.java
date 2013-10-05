package yadf.userinterface.game.guistate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import yadf.controller.AbstractController;
import yadf.simulation.IGameObject;
import yadf.simulation.IPlayer;
import yadf.userinterface.game.IGamePanel;
import yadf.userinterface.game.WorldPanelPopupMenu;

/**
 * The normal GUI state.
 * <p>
 * Allows game objects to be selected.
 */
public class NormalGuiState extends AbstractGuiState implements MouseListener {

    /** The main popup menu. */
    private WorldPanelPopupMenu mainPopupMenu;

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controller, final IGamePanel gamePanel) {
        super.setup(playerTmp, controller, gamePanel);
        gamePanel.getWorldPanel().addMouseListener(this);
        mainPopupMenu = new WorldPanelPopupMenu(gamePanel);
        gamePanel.getWorldPanel().setComponentPopupMenu(mainPopupMenu);
    }

    @Override
    public String toString() {
        return "Normal";
    }

    @Override
    public void interrupt() {
        gamePanel.getWorldPanel().removeMouseListener(this);
        gamePanel.getWorldPanel().setComponentPopupMenu(null);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        IGameObject gameObject = gamePanel.getWorldPanel().getSelectedGameObject();
        if (gameObject != null) {
            gamePanel.getManagementPanel().openGameObjectInterface(gameObject);
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
}
