package simulation.tree;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import misc.MyRandom;
import simulation.IGameObjectListener;
import simulation.IRegion;
import simulation.Tree;
import simulation.map.BlockType;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * The tree manager.
 */
public class TreeManager implements ITreeManager, IGameObjectListener {

    /** The probability that a tile will have a tree. */
    private static final double TREE_PROBABILITY = 0.1;

    /** All of the trees. */
    private final Set<Tree> trees = new CopyOnWriteArraySet<>();

    /** The region that this tree manager is managing trees for. */
    private final IRegion region;

    /** The tree manager listeners, will be notified when a tree is added or removed. */
    private final Set<ITreeManagerListener> listeners = new LinkedHashSet<>();

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
                    trees.add(tree);
                    for (ITreeManagerListener listener : listeners) {
                        listener.treeAdded(tree);
                    }
                    tree.addGameObjectListener(this);
                }
            }
        }
    }

    /**
     * Removes a tree.
     * @param tree the tree to remove
     */
    public void removeTree(final Tree tree) {
        trees.remove(tree);
        for (ITreeManagerListener listener : listeners) {
            listener.treeRemoved(tree);
        }
        tree.removeGameObjectListener(this);
    }

    @Override
    public Tree getTree(final MapIndex mapIndex) {
        for (Tree tree : trees) {
            if (tree.getPosition().equals(mapIndex)) {
                return tree;
            }
        }
        return null;
    }

    /**
     * Get all the trees within an area.
     * @param area the area
     * @return the trees
     */
    public Set<Tree> getTrees(final MapArea area) {
        Set<Tree> treesTmp = new LinkedHashSet<>();
        for (Tree tree : trees) {
            if (area.containesIndex(tree.getPosition())) {
                treesTmp.add(tree);
            }
        }
        return treesTmp;
    }

    @Override
    public void addListener(final ITreeManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void gameObjectDeleted(final Object gameObject) {
        assert trees.contains(gameObject);
        removeTree((Tree) gameObject);
    }
}
