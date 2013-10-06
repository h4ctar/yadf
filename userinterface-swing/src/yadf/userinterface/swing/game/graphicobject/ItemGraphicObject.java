package yadf.userinterface.swing.game.graphicobject;

import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.item.Item;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.userinterface.swing.misc.Sprite;
import yadf.userinterface.swing.misc.SpriteManager;

/**
 * Graphic object to render an item.
 */
public class ItemGraphicObject implements IGraphicObject {

    /** The item. */
    private final Item item;

    /**
     * Constructor.
     * @param itemTmp the item
     */
    public ItemGraphicObject(final IGameObject itemTmp) {
        item = (Item) itemTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapIndex position = item.getPosition();
        if (viewArea.containesIndex(position)) {
            int x = (position.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (position.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
            Sprite itemSprite = SpriteManager.getInstance().getItemSprite(item.getType().sprite);
            itemSprite.draw(graphics, x, y);
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return item.getPosition().equals(mapIndex);
    }
}
