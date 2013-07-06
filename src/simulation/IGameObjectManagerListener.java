package simulation;

public interface IGameObjectManagerListener {
    void gameObjectAdded(IGameObject gameObject);

    void gameObjectRemoved(IGameObject gameObject);
}
