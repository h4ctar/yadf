package yadf.ui.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class AbstractScreen implements Screen {

    protected IScreenController screenController;

    protected Stage stage;

    public AbstractScreen(IScreenController screenControllerTmp) {
        screenController = screenControllerTmp;
    }

    @Override
    public void show() {
        System.out.println("AbstractScreen.show");
        stage = new Stage(0, 0, true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("AbstractScreen.resize");
        stage.setViewport(width, height, true);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
        stage.dispose();
    }
}
