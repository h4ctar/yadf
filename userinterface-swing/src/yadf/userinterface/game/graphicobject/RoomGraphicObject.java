package yadf.userinterface.game.graphicobject;

import java.awt.Color;
import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.item.Item;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.simulation.room.Room;
import yadf.userinterface.misc.Sprite;
import yadf.userinterface.misc.SpriteManager;

/**
 * Graphic object to render a room.
 */
public class RoomGraphicObject implements IGraphicObject {

    /** The colour of a room. */
    private static final Color ROOM_COLOUR = new Color(0.7f, 0.7f, 0.7f, 0.4f);

    /** The room. */
    private final Room room;

    /**
     * Constructor.
     * @param roomTmp the room
     */
    public RoomGraphicObject(final IGameObject roomTmp) {
        room = (Room) roomTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapArea area = room.getArea();
        if (viewArea.operlapsArea(area)) {
            int x = (area.pos.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (area.pos.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
            graphics.setColor(ROOM_COLOUR);
            graphics.fillRect(x, y, area.width * SpriteManager.SPRITE_SIZE, area.height * SpriteManager.SPRITE_SIZE);
            // TODO: draw items using ItemGraphicObject
            for (Item item : room.getItems()) {
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
        return room.getArea().containesIndex(mapIndex);
    }
}
