package yadf.ui.gdx.screen.game.view;

import yadf.simulation.IEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * Entity Image View.
 */
public class EntityImageView<T extends IEntity> extends AbstractEntityView<T> {

    private AtlasRegion region;

    /**
     * Constructor.
     * @param entity the entity
     * @param atlas the texture atlas
     * @param regionName the name of the texture region
     */
    public EntityImageView(final T entity, final TextureAtlas atlas, final String regionName) {
        super(entity);
        region = atlas.findRegion(regionName);
        if (region == null) {
            region = atlas.getRegions().first();
        }
    }

    @Override
    public void draw(final SpriteBatch batch, final float parentAlpha) {
        batch.enableBlending();
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }
}
