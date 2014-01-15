package yadf.ui.gdx.screen.game.toolbar;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.job.IJobManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.CreateStockpileInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;
import yadf.ui.gdx.screen.game.window.JobsWindow;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The top level of the controls stack (the buttons in the top left).
 */
public class MainToolbar extends AbstractToolbar {

    /** The toolbar manager. */
    private IToolbarManager toolbarManager;

    /** The dialog window manager. */
    private IDialogWindowManager dialogWindowManager;

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
     * @param toolbarManagerTmp the toolbar manager
     * @param dialogWindowManagerTmp the dialog window manager
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public MainToolbar(final IToolbarManager toolbarManagerTmp, final IDialogWindowManager dialogWindowManagerTmp,
            final IInteractorManager interactorManagerTmp, final IPlayer playerTmp, final TileCamera cameraTmp,
            final AbstractController controllerTmp) {
        toolbarManager = toolbarManagerTmp;
        dialogWindowManager = dialogWindowManagerTmp;
        interactorManager = interactorManagerTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;

        setFillParent(true);
        align(Align.top | Align.left);
        pad(10);

        Skin skin = toolbarManager.getSkin();

        Button designateButton = addButton("Designate", skin);
        designateButton.addListener(new DesignateButtonListener());

        Button workshopButton = addButton("Build Workshop", skin);
        workshopButton.addListener(new BuildWorkshopButtonListener());

        Button roomButton = addButton("Create Room", skin);
        roomButton.addListener(new CreateRoomButtonListener());

        // Button itemButton = addButton("Place Item", skin);

        Button stockpileButton = addButton("Create Stockpile", skin);
        stockpileButton.addListener(new CreateStockpileButtonListener());

        // Button farmButton = addButton("Build Farm", skin);

        Button jobsButton = addButton("Jobs", skin);
        jobsButton.addListener(new JobsButtonListener());
    }

    /**
     * The listener for the designate button.
     */
    private final class DesignateButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new DesignateToolbar(toolbarManager, interactorManager, player, camera,
                    controller));
        }
    }

    /**
     * The listener for the build workshop button.
     */
    private final class BuildWorkshopButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new BuildWorkshopToolbar(toolbarManager, interactorManager, player, camera,
                    controller));
        }
    }

    /**
     * The listener for the create room button.
     */
    private final class CreateRoomButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new CreateRoomToolbar(toolbarManager, interactorManager, player, camera,
                    controller));
        }
    }

    /**
     * The listener for the create stockpile button.
     */
    private final class CreateStockpileButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            IInteractor interactor = new CreateStockpileInteractor(player, camera, controller);
            interactorManager.installInteractor(interactor);
        }
    }

    /**
     * The listener for the jobs button.
     */
    private final class JobsButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            JobsWindow dialogWindow = new JobsWindow(player.getComponent(IJobManager.class),
                    dialogWindowManager.getSkin());
            dialogWindowManager.setWindow(dialogWindow);
        }
    }
}
