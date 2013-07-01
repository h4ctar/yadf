package simulation;

/**
 * Interface all game objects need to implement.
 */
public interface IGameObject {
    /**
     * Returns the id of this game object.
     * @return The id of the game object
     */
    int getId();

    /**
     * Gets the removes the.
     * @return the removes the
     */
    boolean isDeleted();

    /**
     * Delete the game object.
     */
    void delete();

    /**
     * Add a game object listener that will be notified whenever the game object is deleted.
     * @param listener the new listener
     */
    void addGameObjectListener(IGameObjectListener listener);

    /**
     * Remove a game object listener that was notified if the game object was deleted.
     * @param listener the listener to remove
     */
    void removeGameObjectListener(IGameObjectListener listener);
}
