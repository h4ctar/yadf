package yadf.simulation.tree;

import java.util.Random;

import yadf.misc.MyRandom;
import yadf.simulation.AbstractEntityManager;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.IRegion;
import yadf.simulation.Tree;
import yadf.simulation.map.BlockType;
import yadf.simulation.map.MapIndex;

/**
 * The tree manager.
 */
public class TreeManager extends AbstractEntityManager<Tree> implements ITreeManager, IGameObjectListener {

    /** The probability that a tile will have a tree. */
    private static final double TREE_PROBABILITY = 0.1;

    /** The region that this tree manager is managing trees for. */
    private final IRegion region;

    /**
     * Constructor.
     * @param regionTmp the region that this tree manager is managing trees for
     */
    public TreeManager(final IRegion regionTmp) {
        region = regionTmp;
    }

    /**
     * Adds the trees.
     */
    public void addTrees() {
        Random random = MyRandom.getInstance();
        for (int x = 0; x < region.getMap().getMapSize().x; x++) {
            for (int y = 0; y < region.getMap().getMapSize().y; y++) {
                int z = region.getMap().getHeight(x, y);

                if (region.getMap().getBlock(x, y, z + 1) == BlockType.RAMP) {
                    continue;
                }

                if (random.nextDouble() < TREE_PROBABILITY) {
                    Tree tree = new Tree(new MapIndex(x, y, z));
                    addGameObject(tree);
                }
            }
        }
    }
}
