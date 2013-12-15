package yadf.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract Game Object Manager.
 * @param <T> the type of game object to manage
 */
public abstract class AbstractGameObjectManager<T extends IGameObject> extends AbstractGameObject implements
        IGameObjectManager<T>, IGameObjectListener {

    /** The Game Objects. */
    private final List<T> gameObjects = new ArrayList<>();

    /** The Game Object Manager listeners, notified of add and remove of Game Object. */
    private final List<IGameObjectManagerListener> managerListeners = new ArrayList<>();

    /** The available listeners, notified when a Game Object becomes available. */
    private final List<IGameObjectAvailableListener> availableListeners = new ArrayList<>();

    @Override
    public void addManagerListener(final IGameObjectManagerListener listener) {
        assert !managerListeners.contains(listener);
        managerListeners.add(listener);
    }

    @Override
    public void removeManagerListener(final IGameObjectManagerListener listener) {
        assert managerListeners.contains(listener);
        managerListeners.remove(listener);
    }

    @Override
    public void addAvailableListener(final IGameObjectAvailableListener listener) {
        assert !availableListeners.contains(listener);
        availableListeners.add(listener);
    }

    @Override
    public void removeAvailableListener(final IGameObjectAvailableListener listener) {
        assert availableListeners.contains(listener);
        availableListeners.remove(listener);
    }

    @Override
    public void gameObjectChanged(final IGameObject gameObject) {
        if (gameObject.isAvailable()) {
            notifyGameObjectAvailable(gameObject);
        }
    }

    /**
     * Notify all the listeners that a Game Object is now available.
     * @param gameObject the Game Object that is now available
     */
    private void notifyGameObjectAvailable(final IGameObject gameObject) {
        for (IGameObjectAvailableListener listener : availableListeners) {
            listener.gameObjectAvailable(gameObject);
            if (!gameObject.isAvailable()) {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        if (gameObjects.contains(gameObject)) {
            removeGameObject((T) gameObject);
        }
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
     * Get the game objects.
     * @return the game objects
     */
    protected List<T> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void addGameObject(final T gameObject) {
        assert !gameObjects.contains(gameObject);
        gameObject.addGameObjectListener(this);
        gameObjects.add(gameObject);
        notifyGameObjectAdded(gameObject);
        if (gameObject.isAvailable()) {
            notifyGameObjectAvailable(gameObject);
        }
    }

    @Override
    public void removeGameObject(final T gameObject) {
        assert gameObjects.contains(gameObject);
        gameObject.removeGameObjectListener(this);
        gameObjects.remove(gameObject);
        notifyGameObjectRemoved(gameObject);
    }

    /**
     * Notify all the listeners that a Game Object has been added.
     * @param gameObject the gameObject that was added
     */
    private void notifyGameObjectAdded(final T gameObject) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectAdded(gameObject);
        }
    }

    /**
     * Notify all the listeners that a Game Object has been removed.
     * @param gameObject the gameObject that was added
     */
    private void notifyGameObjectRemoved(final T gameObject) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectRemoved(gameObject);
        }
    }
}
