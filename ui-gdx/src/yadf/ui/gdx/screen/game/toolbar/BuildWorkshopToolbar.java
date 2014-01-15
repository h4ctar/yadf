package yadf.ui.gdx.screen.game.toolbar;

import java.util.Collection;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.workshop.WorkshopType;
import yadf.simulation.workshop.WorkshopTypeManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.BuildWorkshopInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

/**
 * The controls for the build workshop.
 */
public class BuildWorkshopToolbar extends AbstractInteractorToolbar<WorkshopType> {

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
    public BuildWorkshopToolbar(final IToolbarManager toolbarManagerTmp,
            final IInteractorManager interactorManagerTmp, final IPlayer playerTmp, final TileCamera cameraTmp,
            final AbstractController controllerTmp) {
        super(toolbarManagerTmp, interactorManagerTmp);
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;
    }

    @Override
    protected Collection<WorkshopType> getTypes() {
        return WorkshopTypeManager.getInstance().getWorkshopTypes();
    }

    @Override
    protected IInteractor createInteractor(final WorkshopType type) {
        return new BuildWorkshopInteractor(type, player, camera, controller);
    }
}
