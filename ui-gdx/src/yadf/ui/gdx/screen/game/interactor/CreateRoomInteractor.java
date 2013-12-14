package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.CreateRoomCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;
import yadf.ui.gdx.screen.TileCamera;

/**
 * Interactor to create a room.
 */
public class CreateRoomInteractor extends AbstractSelectionInteractor {

    /** The type of room that this interactor will create. */
    private String roomType;

    /** The player that we're modifying the designations of. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param roomTypeTmp the type of room that this interactor will create
     * @param playerTmp the player
     * @param camera the camera
     * @param controllerTmp the controller
     */
    public CreateRoomInteractor(final String roomTypeTmp, final IPlayer playerTmp, final TileCamera camera,
            final AbstractController controllerTmp) {
        super(camera);
        roomType = roomTypeTmp;
        player = playerTmp;
        controller = controllerTmp;
    }

    @Override
    protected void doAction(final MapArea selection) {
        controller.addCommand(new CreateRoomCommand(selection, player, roomType));
    }
}
