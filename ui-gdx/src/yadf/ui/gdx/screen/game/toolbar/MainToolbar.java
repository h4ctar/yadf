package yadf.ui.gdx.screen.game.toolbar;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.job.IJobManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.dialogwindow.IDialogWindowManager;
import yadf.ui.gdx.screen.game.dialogwindow.JobsDialogWindow;
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
public class MainToolbar extends Table {

    /** The skin. */
    private Skin skin;

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
     * @param skinTmp the skin
     * @param toolbarManagerTmp the toolbar manager
     * @param dialogWindowManagerTmp the dialog window manager
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public MainToolbar(final Skin skinTmp, final IToolbarManager toolbarManagerTmp,
            final IDialogWindowManager dialogWindowManagerTmp, final IInteractorManager interactorManagerTmp,
            final IPlayer playerTmp, final TileCamera cameraTmp, final AbstractController controllerTmp) {
        skin = skinTmp;
        toolbarManager = toolbarManagerTmp;
        dialogWindowManager = dialogWindowManagerTmp;
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
        workshopButton.addListener(new BuildWorkshopButtonListener());
        add(workshopButton).width(140).spaceBottom(10);

        row();
        TextButton roomButton = new TextButton("Create Room", skin);
        roomButton.addListener(new CreateRoomButtonListener());
        add(roomButton).width(140).spaceBottom(10);

        row();
        TextButton itemButton = new TextButton("Place Item", skin);
        add(itemButton).width(140).spaceBottom(10);

        row();
        TextButton stockpileButton = new TextButton("Create Stockpile", skin);
        add(stockpileButton).width(140).spaceBottom(10);

        row();
        TextButton farmButton = new TextButton("Build Farm", skin);
        add(farmButton).width(140).spaceBottom(10);

        row();
        TextButton jobsButton = new TextButton("Jobs", skin);
        jobsButton.addListener(new JobsButtonListener());
        add(jobsButton).width(140);
    }

    /**
     * The listener for the designate button.
     */
    private final class DesignateButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new DesignateToolbar(skin, toolbarManager, interactorManager, player,
                    camera, controller));
        }
    }

    /**
     * The listener for the build workshop button.
     */
    private final class BuildWorkshopButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new BuildWorkshopToolbar(skin, toolbarManager, interactorManager, player,
                    camera, controller));
        }
    }

    /**
     * The listener for the create room button.
     */
    private final class CreateRoomButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            toolbarManager.setToolbar(new CreateRoomToolbar(skin, toolbarManager, interactorManager, player,
                    camera, controller));
        }
    }

    /**
     * The listener for the jobs button.
     */
    private final class JobsButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            JobsDialogWindow dialogWindow = new JobsDialogWindow(player.getComponent(IJobManager.class), skin);
            dialogWindowManager.setDialogWindow(dialogWindow);
        }
    }
}
