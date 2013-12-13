package yadf.simulation;

/**
 * An abstract Game Object Manager.
 * @param <T> the type of game object to manage
 */
public abstract class AbstractGameObjectManager<T extends IGameObject> extends AbstractGameObject implements
        IGameObjectManager<T>, IGameObjectListener {

}
