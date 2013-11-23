package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The top level of the controls stack (the buttons in the top left).
 */
public class MainControls extends Table {

    /** The skin. */
    private Skin skin;

    /** The controls controller. */
    private IControlsController controlsController;

    /** The interactor manager. */
    private IInteractorManager interactorManager;

    /** The player. */
    private IPlayer player;

    /** The camera. */
    private TileCamera camera;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param skinTmp the skin
     * @param controlsControllerTmp the controls controller
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public MainControls(final Skin skinTmp, final IControlsController controlsControllerTmp,
            final IInteractorManager interactorManagerTmp, final IPlayer playerTmp, final TileCamera cameraTmp,
            final AbstractController controllerTmp) {
        skin = skinTmp;
        controlsController = controlsControllerTmp;
        interactorManager = interactorManagerTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;

        setFillParent(true);
        align(Align.top | Align.left);
        pad(10);

        TextButton designateButton = new TextButton("Designate", skin);
        designateButton.addListener(new DesignateButtonListener());
        add(designateButton).width(140).spaceBottom(10);

        row();
        TextButton workshopButton = new TextButton("Build Workshop", skin);
        add(workshopButton).width(140).spaceBottom(10);

        row();
        TextButton roomButton = new TextButton("Create Room", skin);
        add(roomButton).width(140).spaceBottom(10);

        row();
        TextButton itemButton = new TextButton("Place Item", skin);
        add(itemButton).width(140).spaceBottom(10);

        row();
        TextButton stockpileButton = new TextButton("Create Stockpile", skin);
        add(stockpileButton).width(140).spaceBottom(10);

        row();
        TextButton farmButton = new TextButton("Build Farm", skin);
        add(farmButton).width(140);
    }

    /**
     * The listener for the designate button.
     */
    private final class DesignateButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            controlsController.setCurrentControls(new DesignateControls(skin, controlsController,
                    interactorManager, player, camera, controller));
        }
    }
}
