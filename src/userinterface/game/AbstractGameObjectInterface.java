package userinterface.game;

import javax.swing.JPanel;

import simulation.IGameObject;
import simulation.IPlayer;
import controller.AbstractController;

/**
 * An abstract game object interface.
 */
public abstract class AbstractGameObjectInterface extends JPanel {

    /** The player. */
    protected IPlayer player;

    /** The controller. */
    protected AbstractController controller;

    /** The game panel. */
    protected IGamePanel gamePanel;

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Setup the game object interface.
     * @param gameObject
     * @param playerTmp
     * @param controllerTmp
     * @param gamePanel
     * @return
     */
    public final void setup(final IGameObject gameObject, final IPlayer playerTmp,
            final AbstractController controllerTmp, final IGamePanel gamePanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        gamePanel = gamePanelTmp;
        setup(gameObject);
    }

    protected abstract void setup(final IGameObject gameObject);

    public abstract String getTitle();
}
