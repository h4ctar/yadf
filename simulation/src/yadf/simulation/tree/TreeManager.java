package yadf.simulation.tree;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import yadf.misc.MyRandom;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.IRegion;
import yadf.simulation.Tree;
import yadf.simulation.map.BlockType;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * The tree manager.
 */
public class TreeManager implements ITreeManager, IGameObjectListener {

    /** The probability that a tile will have a tree. */
    private static final double TREE_PROBABILITY = 0.1;

    /** All of the trees. */
    private final List<Tree> trees = new ArrayList<>();

    /** The region that this tree manager is managing trees for. */
    private final IRegion region;

    /** The tree manager listeners, will be notified when a tree is added or removed. */
    private final List<IGameObjectManagerListener> listeners = new ArrayList<>();

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
                    for (IGameObjectManagerListener listener : listeners) {
                        listener.gameObjectAdded(tree, trees.indexOf(tree));
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
        int index = trees.indexOf(tree);
        trees.remove(tree);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectRemoved(tree, index);
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
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    @Override
    public void gameObjectDeleted(final Object gameObject) {
        assert trees.contains(gameObject);
        removeTree((Tree) gameObject);
    }
}
