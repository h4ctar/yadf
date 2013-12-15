package yadf.simulation;

/**
 * Game Object Available Listener.
 */
public interface IGameObjectAvailableListener {

    /**
     * A Game Object has become available.
     * @param gameObject the available Game Object
     */
    void gameObjectAvailable(IGameObject gameObject);
}
