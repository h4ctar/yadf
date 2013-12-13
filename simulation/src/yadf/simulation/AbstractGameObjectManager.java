package yadf.simulation;

import java.util.ArrayList;
import java.util.List;

// TODO: add the available listener
/**
 * An abstract Game Object Manager.
 * @param <T> the type of game object to manage
 */
public class AbstractGameObjectManager<T extends IGameObject> extends AbstractGameObject implements
        IGameObjectManager<T>, IGameObjectListener {

    /** The game objects. */
    protected final List<T> gameObjects = new ArrayList<>();

    /** Listeners to be notified when something changes. */
    private final List<IGameObjectManagerListener> listeners = new ArrayList<>();

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    @Override
    public List<T> getGameObjects() {
        return gameObjects;
    }

    @Override
    public T getGameObject(final int id) {
        for (T gameObject : gameObjects) {
            if (gameObject.getId() == id) {
                return gameObject;
            }
        }
        return null;
    }

    /**
     * Add a new game object.
     * @param gameObject the game object to add
     */
    public void addGameObject(final T gameObject) {
        assert !gameObjects.contains(gameObject);
        gameObject.addGameObjectListener(this);
        gameObjects.add(gameObject);
        notifyGameObjectAdded(gameObject, gameObjects.indexOf(gameObject));
    }

    /**
     * Remove a game object.
     * @param gameObject the game object to remove
     */
    public void removeGameObject(final T gameObject) {
        assert gameObjects.contains(gameObject);
        gameObject.removeGameObjectListener(this);
        int index = gameObjects.indexOf(gameObject);
        gameObjects.remove(gameObject);
        notifyGameObjectRemoved(gameObject, index);
    }

    /**
     * Notify that a game object was added.
     * @param gameObject the game object that was added
     * @param index the index of the game object once it is added
     */
    private void notifyGameObjectAdded(final IGameObject gameObject, final int index) {
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectAdded(gameObject, index);
        }
    }

    /**
     * Notify that a game object has been removed.
     * @param gameObject the game object that was removed
     * @param index the index of the game object before it was removed
     */
    private void notifyGameObjectRemoved(final IGameObject gameObject, final int index) {
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectRemoved(gameObject, index);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        removeGameObject((T) gameObject);
    }

    @Override
    public void gameObjectChanged(final IGameObject gameObject) {
        // Do nothing
    }

    @Override
    public void gameObjectAvailable(final IGameObject gameObject) {
    }
}
