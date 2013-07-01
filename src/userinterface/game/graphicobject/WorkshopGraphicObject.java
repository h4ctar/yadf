package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.map.MapArea;
import simulation.workshop.Workshop;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

/**
 * Graphic object to render a workshop.
 */
public class WorkshopGraphicObject implements IGraphicObject {

    /** The workshop. */
    private final Workshop workshop;

    /**
     * Constructor.
     * @param workshopTmp the workshop
     */
    public WorkshopGraphicObject(final Workshop workshopTmp) {
        workshop = workshopTmp;
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
}
