package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IEntity;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EntitySolid2d extends Actor {

    /** The entity. */
    private IEntity entity;

    private int width;

    private int height;

    /**
     * Constructor.
     * @param entityTmp the entity
     */
    public EntitySolid2d(final IEntity entityTmp, final int widthTmp, final int heightTmp) {
        entity = entityTmp;
        width = widthTmp;
        height = heightTmp;
        // TODO: all game objects should have width and height
        setWidth(width * GameScreen.SPRITE_SIZE);
        setHeight(height * GameScreen.SPRITE_SIZE);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapIndex position = entity.getPosition();
        setX(position.x * GameScreen.SPRITE_SIZE);
        setY(position.y * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == position.z);
    }

    /**
     * Draw.
     */
    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        Gdx.gl.glEnable(GL10.GL_BLEND);
        shapeRenderer.begin(ShapeType.FilledRectangle);
        shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 0.25f);
        shapeRenderer.filledRect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
        shapeRenderer.begin(ShapeType.Rectangle);
        shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
    }
}
