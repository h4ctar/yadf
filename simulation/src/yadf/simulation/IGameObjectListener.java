package yadf.simulation;

/**
 * Interface for a listener to a game object, they should be notified when the game object is deleted.
 */
public interface IGameObjectListener {

    /**
     * The game object has been deleted.
     * @param gameObject the game object that was deleted
     */
    void gameObjectDeleted(IGameObject gameObject);

    /**
     * The game object has changed.
     * @param gameObject the game object that changed
     */
    void gameObjectChanged(IGameObject gameObject);
}
