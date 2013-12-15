package yadf.simulation;

/**
 * Interface for a listener to a game object manager.
 */
public interface IGameObjectManagerListener {

    /**
     * A game object has been added.
     * @param gameObject the new game object
     */
    void gameObjectAdded(IGameObject gameObject);

    /**
     * A game object has been removed.
     * @param gameObject the old game object
     */
    void gameObjectRemoved(IGameObject gameObject);

    /**
     * A game object has become available.
     * @param gameObject the newly available game object
     */
    void gameObjectAvailable(IGameObject gameObject);
}
