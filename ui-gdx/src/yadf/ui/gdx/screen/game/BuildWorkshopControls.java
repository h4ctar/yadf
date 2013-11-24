package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.workshop.WorkshopType;
import yadf.simulation.workshop.WorkshopTypeManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BuildWorkshopControls extends Table {

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
    public BuildWorkshopControls(final Skin skinTmp, final IControlsController controlsControllerTmp,
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

        for (WorkshopType workshopType : WorkshopTypeManager.getInstance().getWorkshopTypes()) {
            TextButton workshopButton = new TextButton(workshopType.name, skin);
            workshopButton.addListener(new WorkshopButtonListener(workshopType));
            add(workshopButton).width(140).spaceBottom(10);
            row();
        }

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new CancelButtonListener());
        add(cancelButton).width(140);
    }

    /**
     * The listener for all of the build workshop buttons.
     */
    private final class WorkshopButtonListener extends ClickListener {

        /** The type of the workshop. */
        private WorkshopType workshopType;

        /**
         * Constructor.
         * @param workshopTypeTmp the type of the workshop
         */
        public WorkshopButtonListener(final WorkshopType workshopTypeTmp) {
            workshopType = workshopTypeTmp;
        }

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            interactorManager.installInteractor(new BuildWorkshopInteractor(skin, workshopType, player, camera,
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
