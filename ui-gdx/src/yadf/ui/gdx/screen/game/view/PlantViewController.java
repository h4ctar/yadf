package yadf.ui.gdx.screen.game.view;

import yadf.simulation.Tree;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * View Controller for plants.
 */
public class PlantViewController extends AbstractViewController<Tree> {

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    /**
     * Constructor.
     * @param textureAtlasTmp the texture atlas
     * @param gameStage the stage to add the game object 2Ds to
     */
    public PlantViewController(final TextureAtlas textureAtlasTmp, final Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    @Override
    protected Actor createView(final Tree tree) {
        return new EntityImageView<Tree>(tree, textureAtlas, "plant-tree");
    }
}
