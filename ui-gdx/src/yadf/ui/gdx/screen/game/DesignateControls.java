package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.DesignateInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The designate controls.
 * <p>
 * Contains all the buttons for the designations.
 */
public class DesignateControls extends Table {

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

    /** The skin. */
    private Skin skin;

    /**
     * Constructor.
     * @param skinTmp the skin for the buttons
     * @param controlsControllerTmp the controls controller
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public DesignateControls(final Skin skinTmp, final IControlsController controlsControllerTmp,
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

        for (DesignationType designationType : DesignationType.values()) {
            TextButton designateButton = new TextButton(designationType.name, skin);
            designateButton.addListener(new DesignateButtonListener(designationType));
            add(designateButton).width(140).spaceBottom(10);
            row();
        }

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new CancelButtonListener());
        add(cancelButton).width(140);
    }

    /**
     * The listener for all of the designate buttons.
     */
    private final class DesignateButtonListener extends ClickListener {

        /** The type of the designation. */
        private DesignationType designationType;

        /**
         * Constructor.
         * @param designationTypeTmp the type of the designation
         */
        public DesignateButtonListener(final DesignationType designationTypeTmp) {
            designationType = designationTypeTmp;
        }

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            interactorManager.installInteractor(new DesignateInteractor(skin, designationType, player, camera,
                    controller));
        }
    }

    /**
     * The listener for the cancel button.
     */
    private final class CancelButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            controlsController.cancelCurrentControls();
        }
    }
}
