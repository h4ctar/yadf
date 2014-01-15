package yadf.ui.gdx.screen.game.toolbar;

import java.util.Collection;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.room.RoomTypeManager;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.CreateRoomInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

/**
 * The toolbar for the create room buttons.
 */
public class CreateRoomToolbar extends AbstractInteractorToolbar<String> {

    /** The player. */
    private IPlayer player;

    /** The camera. */
    private TileCamera camera;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param skin the skin for the buttons
     * @param controlsController the controls controller
     * @param interactorManager the interactor manager
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public CreateRoomToolbar(final IToolbarManager toolbarManager, final IInteractorManager interactorManager,
            final IPlayer playerTmp, final TileCamera cameraTmp, final AbstractController controllerTmp) {
        super(toolbarManager, interactorManager);
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;
    }

    @Override
    protected Collection<String> getTypes() {
        return RoomTypeManager.getInstance().getRoomTypes();
    }

    @Override
    protected IInteractor createInteractor(String type) {
        return new CreateRoomInteractor(type, player, camera, controller);
    }
}
