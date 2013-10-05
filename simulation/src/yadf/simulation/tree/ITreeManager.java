package yadf.simulation.tree;

import yadf.simulation.IGameObjectManager;
import yadf.simulation.Tree;
import yadf.simulation.map.MapIndex;

/**
 * Interface for a tree manager.
 */
public interface ITreeManager extends IGameObjectManager {

    /**
     * Returns a tree at a specific location if it exists.
     * @param mapIndex The location you want to get a tree from
     * @return A reference to the tree at the location
     */
    Tree getTree(MapIndex mapIndex);
}
