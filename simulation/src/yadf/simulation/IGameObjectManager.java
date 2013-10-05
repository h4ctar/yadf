package yadf.simulation;


/**
 * Interface for a game object manager.
 * <p>
 * Game object managers manage game objects.
 */
public interface IGameObjectManager {
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
