package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IGameObject;
import yadf.simulation.Tree;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Controller for plants.
 */
public class Plant2dController extends Abstract2dController {

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    /**
     * Constructor.
     * @param textureAtlasTmp the texture atlas
     * @param gameStage the stage to add the game object 2Ds to
     */
    public Plant2dController(final TextureAtlas textureAtlasTmp, final Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    @Override
    protected Actor createGameObject2d(final IGameObject gameObject) {
        Tree tree = (Tree) gameObject;
        return new EntityImage2d(tree, textureAtlas, "plants/tree");
    }
}
