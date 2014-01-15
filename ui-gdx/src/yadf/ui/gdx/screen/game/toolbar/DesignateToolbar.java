package yadf.ui.gdx.screen.game.toolbar;

import java.util.Arrays;
import java.util.Collection;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.DesignateInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

/**
 * The designate controls.
 * <p>
 * Contains all the buttons for the designations.
 */
public class DesignateToolbar extends AbstractInteractorToolbar<DesignationType> {

    /** The player. */
    private IPlayer player;

    /** The camera. */
    private TileCamera camera;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param toolbarManagerTmp the toolbar manager
     * @param interactorManagerTmp the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public DesignateToolbar(final IToolbarManager toolbarManagerTmp, final IInteractorManager interactorManagerTmp,
            final IPlayer playerTmp, final TileCamera cameraTmp, final AbstractController controllerTmp) {
        super(toolbarManagerTmp, interactorManagerTmp);
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;
    }

    @Override
    protected Collection<DesignationType> getTypes() {
        return Arrays.asList(DesignationType.values());
    }

    @Override
    protected IInteractor createInteractor(final DesignationType type) {
        return new DesignateInteractor(type, player, camera, controller);
    }
}
