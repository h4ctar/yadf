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
    boolean getRemove();
}
