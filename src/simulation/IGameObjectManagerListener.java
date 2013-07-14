package simulation;

/**
 * Interface for a listener to a game object manager.
 */
public interface IGameObjectManagerListener {

    /**
     * A game object has been added.
     * @param gameObject the new game object
     * @param index the index of the new game object
     */
    void gameObjectAdded(IGameObject gameObject, int index);

    /**
     * A game object has been removed.
     * @param gameObject the old game object
     * @param index the index of the removed game object
     */
    void gameObjectRemoved(IGameObject gameObject, int index);
}
