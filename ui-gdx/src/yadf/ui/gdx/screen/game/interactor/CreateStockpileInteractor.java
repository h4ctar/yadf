package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.CreateStockpileCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapArea;
import yadf.ui.gdx.screen.TileCamera;

public class CreateStockpileInteractor extends AbstractSelectionInteractor {

    /** The player that we're creating a stockpile for. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    public CreateStockpileInteractor(final IPlayer playerTmp, final TileCamera camera,
            final AbstractController controllerTmp) {
        super(camera);
        player = playerTmp;
        controller = controllerTmp;
    }

    @Override
    protected void doAction(final MapArea selection) {
        controller.addCommand(new CreateStockpileCommand(selection, player));
    }
}
