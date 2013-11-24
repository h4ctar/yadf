package yadf.ui.gdx.screen.game.interactor;

import yadf.controller.AbstractController;
import yadf.controller.command.BuildWorkshopCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.map.MapIndex;
import yadf.simulation.workshop.WorkshopType;
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

public class BuildWorkshopInteractor extends AbstractInteractor {

    /** The type of the workshop. */
    private WorkshopType workshopType;

    /** The input processor for the interactor. */
    private WorkshopInputProcessor inputProcessor = new WorkshopInputProcessor();

    /** The player that we're building a workshop for. */
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
     * @param workshopTypeTmp the type of the workshop
     * @param playerTmp the player
     * @param cameraTmp the camera
     * @param controllerTmp the controller
     */
    public BuildWorkshopInteractor(final Skin skinTmp, final WorkshopType workshopTypeTmp,
            final IPlayer playerTmp, final TileCamera cameraTmp, final AbstractController controllerTmp) {
        skin = skinTmp;
        workshopType = workshopTypeTmp;
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
     * Build the workshop.
     * @param position the position of the new workshop
     */
    private void buildWorkshop(final MapIndex position) {
        controller.addCommand(new BuildWorkshopCommand(player, position, workshopType.name));
    }

    @Override
    public void draw() {
        BitmapFont font = skin.getFont("default-font");
        spriteBatch.begin();
        font.draw(spriteBatch, "Build " + workshopType.name, 10, 20);
        spriteBatch.end();
        inputProcessor.draw();
    }

    /**
     * The input processor for the interactor.
     */
    private class WorkshopInputProcessor extends InputAdapter {

        /** The position. */
        private MapIndex position;

        @Override
        public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
            buildWorkshop(position);
            finishInteraction();
            return true;
        }

        @Override
        public boolean mouseMoved(final int screenX, final int screenY) {
            position = camera.getMapIndex(screenX, screenY);
            return true;
        }

        /**
         * Draw.
         */
        public void draw() {
            if (position != null) {
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                int x = position.x * GameScreen.SPRITE_SIZE;
                int y = position.y * GameScreen.SPRITE_SIZE;
                int width = 3 * GameScreen.SPRITE_SIZE;
                int height = 3 * GameScreen.SPRITE_SIZE;
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
    }
}
