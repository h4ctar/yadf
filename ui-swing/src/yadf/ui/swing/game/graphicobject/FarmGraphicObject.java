package yadf.ui.swing.game.graphicobject;

import java.awt.Graphics;
import java.util.List;

import yadf.simulation.IGameObject;
import yadf.simulation.farm.Farm;
import yadf.simulation.farm.FarmPlot;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.swing.misc.Sprite;
import yadf.ui.swing.misc.SpriteManager;

/**
 * Graphic object to render a farm.
 */
public class FarmGraphicObject implements IGraphicObject {
    /** Sprite for till. */
    private static final Sprite TILL_SPRITE = SpriteManager.getInstance().getItemSprite(SpriteManager.TILL_SPRITE);

    /** Sprite for plant. */
    private static final Sprite PLANT_SPRITE = SpriteManager.getInstance().getItemSprite(SpriteManager.PLANT_SPRITE);

    /** Sprite for grow. */
    private static final Sprite GROW_SPRITE = SpriteManager.getInstance().getItemSprite(SpriteManager.GROW_SPRITE);

    /** Sprite for harvest. */
    private static final Sprite HARVEST_SPRITE = SpriteManager.getInstance().getItemSprite(
            SpriteManager.HARVEST_SPRITE);

    /** The farm. */
    private Farm farm;

    /**
     * Constructor.
     * @param farmTmp the farm
     */
    public FarmGraphicObject(final IGameObject farmTmp) {
        farm = (Farm) farmTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapArea area = farm.getArea();

        if (viewArea.operlapsArea(area)) {
            List<FarmPlot> farmPlots = farm.getPlots();
            for (FarmPlot farmPlot : farmPlots) {
                MapIndex pos = farmPlot.getPosition();
                int x = (pos.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
                int y = (pos.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
                switch (farmPlot.getState()) {
                case START:
                    break;
                case TILL:
                    TILL_SPRITE.draw(graphics, x, y);
                    break;
                case PLANT:
                    PLANT_SPRITE.draw(graphics, x, y);
                    break;
                case GROW:
                    GROW_SPRITE.draw(graphics, x, y);
                    break;
                case HARVEST:
                    HARVEST_SPRITE.draw(graphics, x, y);
                    break;
                }
            }
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return farm.getArea().containesIndex(mapIndex);
    }
}
