package yadf.ui.gdx.screen.game.object;

import yadf.simulation.map.MapIndex;
import yadf.simulation.workshop.IWorkshop;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * The game object 2D for a workshop.
 */
public class Workshop2d extends Image {

    /** The workshop. */
    private IWorkshop workshop;

    /**
     * Constructor.
     * @param workshopTmp the workshop
     * @param atlas the texture atlas
     */
    public Workshop2d(final IWorkshop workshopTmp, final TextureAtlas atlas) {
        super(atlas.findRegion("workshops/" + workshopTmp.getType().name.toLowerCase()));
        workshop = workshopTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapIndex position = workshop.getPosition();
        setX(position.x * GameScreen.SPRITE_SIZE);
        setY(position.y * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == position.z);
    }
}
