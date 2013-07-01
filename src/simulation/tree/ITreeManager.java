package simulation.tree;

import simulation.Tree;
import simulation.map.MapIndex;

/**
 * Interface for a tree manager.
 */
public interface ITreeManager {

    /**
     * Returns a tree at a specific location if it exists.
     * @param mapIndex The location you want to get a tree from
     * @return A reference to the tree at the location
     */
    Tree getTree(MapIndex mapIndex);

    /**
     * Add a new tree manager listener that will be notified when a tree is added or removed.
     * @param listener the tree manager listener
     */
    void addListener(ITreeManagerListener listener);
}
