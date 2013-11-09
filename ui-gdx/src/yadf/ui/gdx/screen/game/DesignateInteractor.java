package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.controller.command.DesignationCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

/**
 * Interactor to modify designations.
 */
public class DesignateInteractor extends AbstractInteractor {

    /** The type of the designation. */
    private DesignationType designationType;

    /** The input processor for the interactor. */
    private DesignateInputProcessor inputProcessor = new DesignateInputProcessor();

    /** The player that we're modifying the designations of. */
    private IPlayer player;

    /** The camera. */
    private TileCamera camera;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param designationTypeTmp the type of the designation
     * @param playerTmp the input processor for the interactor
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public DesignateInteractor(final DesignationType designationTypeTmp, final IPlayer playerTmp,
            final TileCamera cameraTmp, final AbstractController controllerTmp) {
        designationType = designationTypeTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    @Override
    public void start() {

    }

    /**
     * Modify the designation.
     * @param selection the selection
     */
    private void designate(final MapArea selection) {
        controller.addCommand(new DesignationCommand(selection, designationType, true, player));
    }

    /**
     * The input processor for the interactor.
     */
    private class DesignateInputProcessor extends InputAdapter {

        private MapIndex start;

        private MapIndex end;

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            start = camera.getMapIndex(screenX, screenY);
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            end = camera.getMapIndex(screenX, screenY);
            MapArea selection = new MapArea();
            selection.width = Math.abs(start.x - end.x) + 1;
            selection.height = Math.abs(start.y - end.y) + 1;
            selection.pos.x = Math.min(start.x, end.x);
            selection.pos.y = Math.min(start.y, end.y);
            selection.pos.z = start.z;
            designate(selection);
            finishInteraction();
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }
    }
}
