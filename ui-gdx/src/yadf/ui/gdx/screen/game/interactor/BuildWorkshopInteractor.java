package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.BuildWorkshopCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;
import yadf.simulation.workshop.WorkshopType;
import yadf.ui.gdx.screen.TileCamera;

/**
 * Interactor to build a workshop.
 */
public class BuildWorkshopInteractor extends AbstractSelectionInteractor {

    /** The type of the workshop. */
    private WorkshopType workshopType;

    /** The player that we're building a workshop for. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param type the type of the workshop
     * @param playerTmp the player
     * @param camera the camera
     * @param controllerTmp the controller
     */
    public BuildWorkshopInteractor(final WorkshopType type, final IPlayer playerTmp, final TileCamera camera,
            final AbstractController controllerTmp) {
        super(camera, 3);
        workshopType = type;
        player = playerTmp;
        controller = controllerTmp;
    }

    @Override
    protected void doAction(final MapArea selection) {
        controller.addCommand(new BuildWorkshopCommand(player, selection.pos, workshopType));
    }
}
