package simulation.tree;

import simulation.Tree;

/**
 * Interface for a tree manager listener.
 */
public interface ITreeManagerListener {

    /**
     * A tree has been added.
     * @param tree the new tree
     */
    void treeAdded(Tree tree);

    /**
     * A tree has been removed.
     * @param tree the removed tree
     */
    void treeRemoved(Tree tree);
}
