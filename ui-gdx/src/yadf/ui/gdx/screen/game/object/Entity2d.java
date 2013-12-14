package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IEntity;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Abstract Entity 2D.
 */
public class Entity2d extends Actor {

    /** The entity. */
    private IEntity entity;

    /** The image. */
    private Image image;

    /**
     * Constructor.
     * @param entityTmp the entity
     * @param atlas the texture atlas
     * @param regionName the name of the texture region
     */
    public Entity2d(final IEntity entityTmp, final TextureAtlas atlas, final String regionName) {
        entity = entityTmp;
        AtlasRegion region = atlas.findRegion(regionName);
        if (region == null) {
            region = atlas.getRegions().first();
        }
        image = new Image(region);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapIndex position = entity.getPosition();
        int x = position.x * GameScreen.SPRITE_SIZE;
        int y = position.y * GameScreen.SPRITE_SIZE;
        boolean visible = (int) getStage().getCamera().position.z == position.z;
        setPosition(x, y);
        setVisible(visible);
        image.setPosition(x, y);
        image.setVisible(visible);
    }

    @Override
    public void draw(final SpriteBatch batch, final float parentAlpha) {
        image.draw(batch, parentAlpha);
    }
}
