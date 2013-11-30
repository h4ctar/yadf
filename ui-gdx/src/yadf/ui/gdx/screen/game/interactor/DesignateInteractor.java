package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.DesignationCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.simulation.map.MapArea;
import yadf.ui.gdx.screen.TileCamera;

/**
 * Interactor to modify designations.
 */
public class DesignateInteractor extends AbstractSelectionInteractor {

    /** The type of the designation. */
    private DesignationType designationType;

    /** The player that we're modifying the designations of. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param type the type of designation
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public DesignateInteractor(final DesignationType type, final IPlayer playerTmp, final TileCamera cameraTmp,
            final AbstractController controllerTmp) {
        super(cameraTmp);
        designationType = type;
        player = playerTmp;
        controller = controllerTmp;
    }

    @Override
    protected void doAction(final MapArea selection) {
        controller.addCommand(new DesignationCommand(selection, designationType, true, player));
    }
}
