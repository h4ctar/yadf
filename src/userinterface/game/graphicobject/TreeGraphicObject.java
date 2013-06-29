package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.Tree;
import simulation.map.MapIndex;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

public class TreeGraphicObject implements IGraphicObject {

    private final Tree tree;

    public TreeGraphicObject(final Tree treeTmp) {
        tree = treeTmp;
    }

    @Override
    public void render(final Graphics graphics, MapIndex viewPosition, MapIndex viewSize) {
        Sprite treeSprite = SpriteManager.getInstance().getItemSprite(SpriteManager.TREE_SPRITE);
        MapIndex position = tree.getPosition();
        if (position.z == viewPosition.z) {
            int x = position.x - viewPosition.x;
            int y = position.y - viewPosition.y;
            if (x >= 0 && x < viewSize.x && y >= 0 && y < viewSize.y) {
                treeSprite.draw(graphics, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
            }
        }
    }
}
