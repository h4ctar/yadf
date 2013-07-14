package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.IGameObject;
import simulation.character.IGameCharacter;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

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
