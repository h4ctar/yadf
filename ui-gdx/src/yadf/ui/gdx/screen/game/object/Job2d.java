package yadf.ui.gdx.screen.game.object;

import yadf.simulation.map.MapArea;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Job2d extends Actor {

    private MapArea mapArea;

    public Job2d(MapArea mapAreaTmp) {
        mapArea = mapAreaTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        int x = mapArea.pos.x * GameScreen.SPRITE_SIZE;
        int y = mapArea.pos.y * GameScreen.SPRITE_SIZE;
        boolean visible = (int) getStage().getCamera().position.z == mapArea.pos.z;
        setPosition(x, y);
        setVisible(visible);
    }

    /**
     * Draw.
     */
    public void draw() {
        if (mapArea != null) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
            int x = mapArea.pos.x * GameScreen.SPRITE_SIZE;
            int y = mapArea.pos.y * GameScreen.SPRITE_SIZE;
            int width = mapArea.width * GameScreen.SPRITE_SIZE;
            int height = mapArea.height * GameScreen.SPRITE_SIZE;
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
