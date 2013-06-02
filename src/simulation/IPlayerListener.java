package simulation;

/**
 * Interface for object that want to listen to a player.
 */
public interface IPlayerListener {

    /**
     * Called when the player changes in some way.
     * @param gameObject the game object that has changed
     * @param added true if the object was added, false otherwise
     */
    void playerChanged(AbstractGameObject gameObject, boolean added);
}
