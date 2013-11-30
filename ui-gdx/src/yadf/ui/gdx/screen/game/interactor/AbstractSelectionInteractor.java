package yadf.ui.gdx.screen.game.interactor;

import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * An abstract Selection Interactor.
 * <p>
 * A selection interactor allows the user to drag an area with the mouse and create a command with the selection.
 */
public abstract class AbstractSelectionInteractor extends AbstractInteractor {

    /** The input processor for the interactor. */
    private SelectionInputProcessor inputProcessor = new SelectionInputProcessor();

    /** The camera. */
    private TileCamera camera;

    /**
     * Constructor.
     * @param cameraTmp the camera
     */
    public AbstractSelectionInteractor(final TileCamera cameraTmp) {
        camera = cameraTmp;
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
     * Do the action.
     * @param selection the selection
     */
    protected abstract void doAction(MapArea selection);

    @Override
    public void draw() {
        inputProcessor.draw();
    }

    /**
     * The input processor for the interactor.
     */
    private class SelectionInputProcessor extends InputAdapter {

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
            doAction(selection);
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
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                int x = selection.pos.x * GameScreen.SPRITE_SIZE;
                int y = selection.pos.y * GameScreen.SPRITE_SIZE;
                int width = selection.width * GameScreen.SPRITE_SIZE;
                int height = selection.height * GameScreen.SPRITE_SIZE;
                Gdx.gl.glEnable(GL10.GL_BLEND);
                shapeRenderer.begin(ShapeType.FilledRectangle);
                shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 0.25f);
                shapeRenderer.filledRect(x, y, width, height);
                shapeRenderer.end();
                Gdx.gl.glDisable(GL10.GL_BLEND);
                shapeRenderer.begin(ShapeType.Rectangle);
                shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.end();
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
