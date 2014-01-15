package yadf.ui.gdx.screen.game.view;

import yadf.simulation.IEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class EntitySolidView<T extends IEntity> extends AbstractEntityView<T> {

    /**
     * Constructor.
     * @param entity the entity
     */
    public EntitySolidView(final T entity) {
        super(entity);
    }

    @Override
    public void draw(final SpriteBatch batch, final float parentAlpha) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        Gdx.gl.glDisable(GL10.GL_BLEND);
        shapeRenderer.begin(ShapeType.Rectangle);
        shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
    }
}
