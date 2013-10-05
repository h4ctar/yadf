package yadf.userinterface.game.graphicobject;

import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.userinterface.misc.Sprite;
import yadf.userinterface.misc.SpriteManager;

/**
 * Graphic object to render a zombie.
 */
public class GoblinGraphicObject implements IGraphicObject {

    /** The dwarf. */
    private final IGameCharacter goblin;

    /**
     * Constructor.
     * @param goblinTmp the goblin
     */
    public GoblinGraphicObject(final IGameObject goblinTmp) {
        goblin = (IGameCharacter) goblinTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapIndex position = goblin.getPosition();
        if (viewArea.containesIndex(position)) {
            int x = (position.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (position.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
            Sprite goblinSprite = SpriteManager.getInstance().getItemSprite(0);
            goblinSprite.draw(graphics, x, y);
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return goblin.getPosition().equals(mapIndex);
    }
}
