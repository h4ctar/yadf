package userinterface.game.graphicobject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

/**
 * Graphic object to render a stockpile, including all the items inside the stockpile.
 */
public class StockpileGraphicObject implements IGraphicObject {

    /** The colour of a stockpile. */
    private static final Color STOCKPILE_COLOUR = new Color(0.7f, 0.6f, 0.5f, 0.4f);

    /** The stockpile. */
    private final Stockpile stockpile;

    /**
     * Constructor.
     * @param stockpileTmp the stockpile
     */
    public StockpileGraphicObject(final Stockpile stockpileTmp) {
        stockpile = stockpileTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapArea stockpileArea = stockpile.getArea();
        if (viewArea.operlapsArea(stockpileArea)) {
            int x = (stockpileArea.pos.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (stockpileArea.pos.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;

            graphics.setColor(STOCKPILE_COLOUR);
            graphics.fillRect(x, y, stockpileArea.width * SpriteManager.SPRITE_SIZE, stockpileArea.height
                    * SpriteManager.SPRITE_SIZE);

            // TODO: render with item graphic object
            Set<Item> items = stockpile.getItems();
            for (Item item : items) {
                MapIndex position = item.getPosition();
                int x2 = (position.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
                int y2 = (position.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
                Sprite itemSprite = SpriteManager.getInstance().getItemSprite(item.getType().sprite);
                itemSprite.draw(graphics, x2, y2);
            }
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return stockpile.getArea().containesIndex(mapIndex);
    }
}
