package yadf.userinterface.game.graphicobject;

import java.awt.Graphics;

import yadf.simulation.IGameObject;
import yadf.simulation.Tree;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.userinterface.misc.Sprite;
import yadf.userinterface.misc.SpriteManager;

/**
 * Graphic object to render a tree.
 */
public class TreeGraphicObject implements IGraphicObject {

    /** The tree sprite. */
    private static final Sprite TREE_SPRITE = SpriteManager.getInstance().getItemSprite(SpriteManager.TREE_SPRITE);

    /** The tree. */
    private final Tree tree;

    /**
     * Constructor.
     * @param treeTmp the tree
     */
    public TreeGraphicObject(final IGameObject treeTmp) {
        tree = (Tree) treeTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        MapIndex position = tree.getPosition();
        if (viewArea.containesIndex(position)) {
            int x = position.x - viewArea.pos.x;
            int y = position.y - viewArea.pos.y;
            TREE_SPRITE.draw(graphics, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
        }
    }

    @Override
    public boolean containsIndex(final MapIndex mapIndex) {
        return tree.getPosition().equals(mapIndex);
    }
}
