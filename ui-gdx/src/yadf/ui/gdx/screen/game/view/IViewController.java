package yadf.ui.gdx.screen.game.view;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManager;
import yadf.simulation.IGameObjectManagerListener;

/**
 * View Controller.
 * <p>
 * View Controllers create Views for Game Objects whenever one is added to a Game Object Manager.
 * @param <T> the type of Game Object the View Controller is creating views for.
 */
public interface IViewController<T extends IGameObject> extends IGameObjectManagerListener<T> {

    /**
     * Add a manager that this view controller will listen to.
     * @param gameObjectManager the game object manager to add
     */
    void addManager(IGameObjectManager<T> gameObjectManager);
}
