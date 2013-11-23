package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.DesignationCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

    /** The skin. */
    private Skin skin;

    /** The sprite batch. */
    private SpriteBatch spriteBatch = new SpriteBatch();

    /**
     * Constructor.
     * @param skinTmp the skin
     * @param designationTypeTmp the type of the designation
     * @param playerTmp the input processor for the interactor
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public DesignateInteractor(final Skin skinTmp, final DesignationType designationTypeTmp,
            final IPlayer playerTmp, final TileCamera cameraTmp, final AbstractController controllerTmp) {
        skin = skinTmp;
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
        // Do nothing.
    }

    /**
     * Modify the designation.
     * @param selection the selection
     */
    private void designate(final MapArea selection) {
        controller.addCommand(new DesignationCommand(selection, designationType, true, player));
    }

    @Override
    public void draw() {
        BitmapFont font = skin.getFont("default-font");
        spriteBatch.begin();
        font.draw(spriteBatch, "Designate " + designationType.name, 10, 20);
        spriteBatch.end();
        inputProcessor.draw();
    }

    /**
     * The input processor for the interactor.
     */
    private class DesignateInputProcessor extends InputAdapter {

        /** The selection. */
        MapArea selection = new MapArea();

        /** The start point. */
        private MapIndex start;

        /** The end point. */
        private MapIndex end;

        @Override
        public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
            start = camera.getMapIndex(screenX, screenY);
            end = camera.getMapIndex(screenX, screenY);
            updateSelection();
            return true;
        }

        @Override
        public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
            designate(selection);
            finishInteraction();
            return true;
        }

        @Override
        public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
            end = camera.getMapIndex(screenX, screenY);
            updateSelection();
            return true;
        }

        /**
         * Draw.
         */
        public void draw() {
            if (start != null && end != null) {
                Gdx.gl.glEnable(GL10.GL_BLEND);
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeType.FilledRectangle);
                shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 0.5f);
                shapeRenderer.filledRect(selection.pos.x * GameScreen.SPRITE_SIZE, selection.pos.y
                        * GameScreen.SPRITE_SIZE, selection.width * GameScreen.SPRITE_SIZE, selection.height
                        * GameScreen.SPRITE_SIZE);
                shapeRenderer.end();
                Gdx.gl.glDisable(GL10.GL_BLEND);
            }
        }

        /**
         * Update the selection.
         */
        private void updateSelection() {
            selection.width = Math.abs(start.x - end.x) + 1;
            selection.height = Math.abs(start.y - end.y) + 1;
            selection.pos.x = Math.min(start.x, end.x);
            selection.pos.y = Math.min(start.y, end.y);
            selection.pos.z = start.z;
        }
    }
}
