package simulation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logger.Logger;

/**
 * An abstract player that implements the boring stuff.
 * @author Ben
 * 
 */
public abstract class AbstractPlayer extends AbstractGameObject implements IPlayer {

    /** The name of the player. */
    private final String name;

    /** All the components. */
    private final Map<Class<? extends IPlayerComponent>, IPlayerComponent> components = new ConcurrentHashMap<>();

    /**
     * Constructor.
     * @param nameTmp the players name
     */
    public AbstractPlayer(final String nameTmp) {
        name = nameTmp;
    }

    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IPlayerComponent> T getComponent(final Class<T> componentInterface) {
        return (T) components.get(componentInterface);
    }

    @Override
    public <T extends IPlayerComponent> void setComponent(final Class<T> componentInterface, final T component) {
        Logger.getInstance().log(
                this,
                "Set component: " + componentInterface.getSimpleName() + " = "
                        + component.getClass().getSimpleName());
        components.put(componentInterface, component);
    }

    @Override
    public <T extends IPlayerComponent> void removeComponent(final Class<T> componentInterface) {
        components.remove(componentInterface);
    }

    @Override
    public void update() {
        for (IPlayerComponent component : components.values()) {
            component.update();
        }
    }
}
