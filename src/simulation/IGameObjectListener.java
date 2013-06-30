package simulation;

/**
 * Interface for a listener to a game object, they should be notified when the game object is deleted.
 */
public interface IGameObjectListener {
    void gameObjectDeleted(IGameObject gameObject);
}
