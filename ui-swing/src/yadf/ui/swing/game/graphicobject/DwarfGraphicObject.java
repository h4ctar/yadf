package yadf.ui.swing.game.graphicobject;

import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.ISkillComponent;
import yadf.simulation.labor.LaborType;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.swing.misc.Sprite;
import yadf.ui.swing.misc.SpriteManager;

/**
 * Graphic object to render a dwarf.
 */
public class DwarfGraphicObject implements IGraphicObject {

    /** The dwarf. */
    private final IGameCharacter dwarf;

    /**
     * Constructor.
     * @param dwarfTmp the dwarf
     */
    public DwarfGraphicObject(final IGameObject dwarfTmp) {
        dwarf = (IGameCharacter) dwarfTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapIndex position = dwarf.getPosition();
        if (viewArea.containesIndex(position)) {
            int x = (position.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
            int y = (position.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
            Sprite dwarfSprite;
            if (dwarf.isDead()) {
                dwarfSprite = SpriteManager.getInstance().getItemSprite(SpriteManager.DEAD_DWARF_SPRITE);
            } else {
                LaborType profession = dwarf.getComponent(ISkillComponent.class).getProfession();
                dwarfSprite = SpriteManager.getInstance().getItemSprite(profession.sprite);
            }
            dwarfSprite.draw(graphics, x, y);
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return dwarf.getPosition().equals(mapIndex);
    }
}
