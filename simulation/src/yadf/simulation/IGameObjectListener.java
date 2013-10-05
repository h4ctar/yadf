package yadf.simulation;

/**
 * Interface for a listener to a game object, they should be notified when the game object is deleted.
 */
public interface IGameObjectListener {

    /**
     * The game object has been deleted.
     * @param gameObject the game object that was deleted
     */
    void gameObjectDeleted(Object gameObject);
}
