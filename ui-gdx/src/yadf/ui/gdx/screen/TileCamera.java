package yadf.ui.gdx.screen;

import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * The camera.
 */
public class TileCamera extends OrthographicCamera {

    /**
     * Zoom to a map index.
     * @param positionTmp the position to zoom to
     */
    public void zoomToPosition(final MapIndex positionTmp) {
        System.out.println("TileCamera.zoomToPosition(" + positionTmp + ")");
        position.set(positionTmp.x * GameScreen.SPRITE_SIZE, positionTmp.y * GameScreen.SPRITE_SIZE, positionTmp.z);
        update();
    }

    /**
     * Resize the view area.
     * @param width the width in pixles
     * @param height the height in pixles
     */
    public void resize(final int width, final int height) {
        System.out.println("TileCamera.resize(" + width + ", " + height + ")");
        viewportWidth = width;
        viewportHeight = height;
        update();
    }

    /**
     * Get the map index from screen coordinates.
     * @param screenX the x coordinate
     * @param screenY the y coordinate
     * @return the map index
     */
    public MapIndex getMapIndex(final int screenX, final int screenY) {
        return new MapIndex((int) (position.x - viewportWidth / 2 + screenX) / GameScreen.SPRITE_SIZE,
                (int) (position.y + viewportHeight / 2 - screenY) / GameScreen.SPRITE_SIZE, (int) (position.z));
    }
}
