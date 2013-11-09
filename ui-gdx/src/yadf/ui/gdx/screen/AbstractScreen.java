package yadf.ui.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The abstract screen class.
 * <p>
 * Has default implementations of the screen methods and a stage.
 */
public class AbstractScreen implements Screen {

    /** The screen controller. */
    protected IScreenController screenController;

    /** The stage for the UI components. */
    protected Stage uiStage;

    /** The input multiplexer. */
    protected InputMultiplexer inputMultiplexer;

    /**
     * Constructor.
     * @param screenControllerTmp the screen controller.
     */
    public AbstractScreen(final IScreenController screenControllerTmp) {
        screenController = screenControllerTmp;
    }

    @Override
    public void show() {
        System.out.println("AbstractScreen.show");
        uiStage = new Stage();
        inputMultiplexer = new InputMultiplexer(uiStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(final int width, final int height) {
        System.out.println("AbstractScreen.resize");
        uiStage.setViewport(width, height, false);
    }

    @Override
    public void render(final float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        draw();
        uiStage.draw();
    }

    /**
     * Update method.
     * <p>
     * Called everytime the screen is rendered.
     * @param delta the time in seconds since the last update
     */
    protected void update(final float delta) {
        uiStage.act(delta);
    }

    /**
     * Draw the background.
     * <p>
     * Called before the stage is drawn.
     */
    protected void draw() {
    }

    @Override
    public void hide() {
        System.out.println("AbstractScreen.hide");
    }

    @Override
    public void pause() {
        System.out.println("AbstractScreen.pause");
    }

    @Override
    public void resume() {
        System.out.println("AbstractScreen.resume");
    }

    @Override
    public void dispose() {
        System.out.println("AbstractScreen.dispose");
        uiStage.dispose();
    }
}
