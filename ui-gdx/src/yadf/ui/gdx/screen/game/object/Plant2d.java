package yadf.ui.gdx.screen.game.object;

import yadf.simulation.Tree;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A plant actor.
 */
public class Plant2d extends Image {

    /** The tree. */
    private Tree tree;

    /**
     * Constructor.
     * @param treeTmp the tree
     * @param atlas the texture atlas
     */
    public Plant2d(final Tree treeTmp, final TextureAtlas atlas) {
        super(atlas.findRegion("plants/tree"));
        tree = treeTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapIndex position = tree.getPosition();
        setX(position.x * GameScreen.SPRITE_SIZE);
        setY(position.y * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == position.z);
    }
}