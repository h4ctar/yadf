package yadf.simulation;

import java.util.List;

/**
 * Interface for a game object manager.
 * <p>
 * Game object managers manage game objects.
 * @param <T> The type of game object that is being managed
 */
public interface IGameObjectManager<T extends IGameObject> {

    /**
     * Add a new farm manager listener who will be notified when an object is added or removed or becomes available.
     * @param listener the new listener
     */
    void addManagerListener(IGameObjectManagerListener<T> listener);

    /**
     * Remove a farm manager listener.
     * @param listener the listener to remove
     */
    void removeManagerListener(IGameObjectManagerListener<T> listener);

    void addAvailableListener(IGameObjectAvailableListener listener);

    void removeAvailableListener(IGameObjectAvailableListener listener);

    /**
     * Get the game objects.
     * @return the game objects
     */
    List<T> getGameObjects();

    T getGameObject(int id);

    void addGameObject(T gameObject);

    void removeGameObject(T gameObject);
}
