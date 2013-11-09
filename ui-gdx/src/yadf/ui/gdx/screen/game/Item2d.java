package yadf.ui.gdx.screen.game;

import yadf.simulation.item.Item;
import yadf.simulation.map.MapIndex;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Item2d extends Image {

    /** The item. */
    private Item item;

    /**
     * Constructor.
     * @param itemTmp the item
     * @param atlas the texture atlas
     */
    public Item2d(Item itemTmp, TextureAtlas atlas) {
        super(atlas.findRegion("items/" + itemTmp.getType().name.toLowerCase()));
        item = itemTmp;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        MapIndex position = item.getPosition();
        setX(position.x * GameScreen.SPRITE_SIZE);
        setY(position.y * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == position.z);
    }
}
