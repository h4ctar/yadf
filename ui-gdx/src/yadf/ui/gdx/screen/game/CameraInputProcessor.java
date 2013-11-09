package yadf.ui.gdx.screen.game;

import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.InputProcessor;

public class CameraInputProcessor implements InputProcessor {

    private TileCamera camera;
    private int lastX;
    private int lastY;

    public CameraInputProcessor(TileCamera cameraTmp) {
        camera = cameraTmp;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastX = screenX;
        lastY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.position.x -= screenX - lastX;
        camera.position.y += screenY - lastY;
        camera.update();
        lastX = screenX;
        lastY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.position.z += amount;
        camera.update();
        return true;
    }
}
