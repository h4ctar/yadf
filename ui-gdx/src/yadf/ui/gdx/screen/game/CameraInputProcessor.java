package yadf.ui.gdx.screen.game;

import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.InputProcessor;

/**
 * Input processor for the camera.
 */
public class CameraInputProcessor implements InputProcessor {

    /** The camera. */
    private TileCamera camera;

    /** The last x position of the mouse. */
    private int lastX;

    /** The last y position of the mouse. */
    private int lastY;

    /**
     * Constructor.
     * @param cameraTmp the camera
     */
    public CameraInputProcessor(final TileCamera cameraTmp) {
        camera = cameraTmp;
    }

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        lastX = screenX;
        lastY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        camera.position.x -= screenX - lastX;
        camera.position.y += screenY - lastY;
        camera.update();
        lastX = screenX;
        lastY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        camera.position.z += amount;
        camera.update();
        return true;
    }
}
