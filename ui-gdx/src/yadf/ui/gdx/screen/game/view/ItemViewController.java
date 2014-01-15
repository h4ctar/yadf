package yadf.ui.gdx.screen.game.view;

import yadf.simulation.item.Item;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * View Controller for items.
 */
public class ItemViewController extends AbstractViewController<Item> {

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    /**
     * Constructor.
     * @param textureAtlasTmp the texture atlas
     * @param gameStage the stage to add the game object 2Ds to
     */
    public ItemViewController(final TextureAtlas textureAtlasTmp, final Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    @Override
    protected Actor createView(final Item item) {
        return new EntityImageView<Item>(item, textureAtlas, "item-" + item.getType().name.toLowerCase());
    }
}
