package yadf.simulation;

/**
 * Interface for a game object manager.
 * <p>
 * Game object managers manage game objects.
 * @param <T> The type of game object that is being managed
 */
public interface IGameObjectManager<T extends IGameObject> {
    /**
     * Add a new farm manager listener who will be notified when a farm is added or removed.
     * @param listener the new listener
     */
    void addGameObjectManagerListener(IGameObjectManagerListener listener);

    /**
     * Remove a farm manager listener.
     * @param listener the listener to remove
     */
    void removeGameObjectManagerListener(IGameObjectManagerListener listener);
}
