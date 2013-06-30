package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.Tree;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

/**
 * Graphic object to render a tree.
 */
public class TreeGraphicObject implements IGraphicObject {

    /** The tree. */
    private final Tree tree;

    /**
     * Constructor.
     * @param treeTmp the tree
     */
    public TreeGraphicObject(final Tree treeTmp) {
        tree = treeTmp;
    }

    @Override
    public void render(final Graphics graphics, final MapArea viewArea) {
        Sprite treeSprite = SpriteManager.getInstance().getItemSprite(SpriteManager.TREE_SPRITE);
        MapIndex position = tree.getPosition();
        if (viewArea.containesIndex(position)) {
            int x = position.x - viewArea.pos.x;
            int y = position.y - viewArea.pos.y;
            treeSprite.draw(graphics, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
        }
    }
}
