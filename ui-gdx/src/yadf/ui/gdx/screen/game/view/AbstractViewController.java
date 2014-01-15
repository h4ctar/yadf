package yadf.ui.gdx.screen.game.view;

import java.util.HashMap;
import java.util.Map;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * A View Controller.
 * <p>
 * The View Controller creates Views whenever there in a new game object in the simulation, it also removes them.
 */
public abstract class AbstractViewController<T extends IGameObject> implements IViewController<T> {

    /** The game object 2Ds that this controller has created. */
    private Map<T, Actor> gameObject2ds = new HashMap<>();

    /** The stage to add the game object 2Ds to. */
    private Stage gameStage;

    /**
     * Constructor.
     * @param gameStageTmp the stage to add the game object 2Ds to
     */
    public AbstractViewController(final Stage gameStageTmp) {
        gameStage = gameStageTmp;
    }

    @Override
    public void gameObjectAdded(final T gameObject) {
        assert !gameObject2ds.containsKey(gameObject);
        Actor gameObject2d = createView(gameObject);
        if (gameObject2d != null) {
            gameObject2ds.put(gameObject, gameObject2d);
            gameStage.addActor(gameObject2d);
        }
    }

    @Override
    public void gameObjectRemoved(final T gameObject) {
        Actor gameObject2d = gameObject2ds.remove(gameObject);
        gameStage.getRoot().removeActor(gameObject2d);
    }

    @Override
    public void gameObjectAvailable(final T gameObject) {
        // Do nothing
    }

    public void addManager(IGameObjectManager<T> gameObjectManager) {
        for (T gameObject : gameObjectManager.getGameObjects()) {
            gameObjectAdded(gameObject);
        }
        gameObjectManager.addManagerListener(this);
    }

    /**
     * Create the game object 2D.
     * @param gameObject the game object to create the game object 2D for
     * @return the game object 2D TODO: rename method
     */
    protected abstract Actor createView(final T gameObject);
}
