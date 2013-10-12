package yadf.ui.swing.game.graphicobject;

import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.simulation.workshop.IWorkshop;
import yadf.simulation.workshop.Workshop;
import yadf.ui.swing.misc.Sprite;
import yadf.ui.swing.misc.SpriteManager;

/**
 * Graphic object to render a workshop.
 */
public class WorkshopGraphicObject implements IGraphicObject {

    /** The workshop. */
    private final IWorkshop workshop;

    /**
     * Constructor.
     * @param workshopTmp the workshop
     */
    public WorkshopGraphicObject(final IGameObject workshopTmp) {
        workshop = (IWorkshop) workshopTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapArea area = new MapArea(workshop.getPosition(), Workshop.WORKSHOP_SIZE, Workshop.WORKSHOP_SIZE);
        if (viewArea.operlapsArea(area)) {
            int x = (area.pos.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (area.pos.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
            Sprite workshopSprite = SpriteManager.getInstance().getWorkshopSprite(workshop.getType().sprite);
            workshopSprite.draw(graphics, x, y);
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return workshop.getArea().containesIndex(mapIndex);
    }
}
