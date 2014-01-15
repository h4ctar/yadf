package yadf.ui.gdx.screen.game.view;

import yadf.simulation.IEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Entity Image View.
 */
public class EntityImageView<T extends IEntity> extends AbstractEntityView<T> {

    /** The image. */
    private Image image;

    /**
     * Constructor.
     * @param entity the entity
     * @param atlas the texture atlas
     * @param regionName the name of the texture region
     */
    public EntityImageView(final T entity, final TextureAtlas atlas, final String regionName) {
        super(entity);
        AtlasRegion region = atlas.findRegion(regionName);
        if (region == null) {
            region = atlas.getRegions().first();
        }
        image = new Image(region);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.setX(getX());
        image.setY(getY());
        image.setWidth(getWidth());
        image.setHeight(getHeight());
    }

    @Override
    public void draw(final SpriteBatch batch, final float parentAlpha) {
        batch.enableBlending();
        image.draw(batch, parentAlpha);
    }
}
