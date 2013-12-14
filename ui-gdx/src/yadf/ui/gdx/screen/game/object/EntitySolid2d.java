package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IEntity;
import yadf.simulation.map.MapArea;
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

    /**
     * Constructor.
     * @param entityTmp the entity
     */
    public EntitySolid2d(final IEntity entityTmp) {
        entity = entityTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapArea area = entity.getArea();
        setX(area.pos.x * GameScreen.SPRITE_SIZE);
        setY(area.pos.y * GameScreen.SPRITE_SIZE);
        setWidth(area.width * GameScreen.SPRITE_SIZE);
        setHeight(area.height * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == area.pos.z);
    }

    @Override
    public void draw(final SpriteBatch batch, final float parentAlpha) {
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
