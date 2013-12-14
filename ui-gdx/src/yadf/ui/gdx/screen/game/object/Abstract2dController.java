package yadf.ui.gdx.screen.game.object;

import java.util.HashMap;
import java.util.Map;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManagerListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * A game object 2D controller.
 * <p>
 * The game object 2D controller creates game object 2Ds whenever there in a new game object in the simulation, it also
 * removes them.
 */
public abstract class Abstract2dController implements IGameObjectManagerListener {

    /** The game object 2Ds that this controller has created. */
    private Map<IGameObject, Actor> gameObject2ds = new HashMap<>();

    /** The stage to add the game object 2Ds to. */
    private Stage gameStage;

    /**
     * Constructor.
     * @param gameStageTmp the stage to add the game object 2Ds to
     */
    public Abstract2dController(final Stage gameStageTmp) {
        gameStage = gameStageTmp;
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject, final int index) {
        assert !gameObject2ds.containsKey(gameObject);
        Actor gameObject2d = createGameObject2d(gameObject);
        if (gameObject2d != null) {
            gameObject2ds.put(gameObject, gameObject2d);
            gameStage.addActor(gameObject2d);
        }
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject, final int index) {
        Actor gameObject2d = gameObject2ds.remove(gameObject);
        gameStage.getRoot().removeActor(gameObject2d);
    }

    @Override
    public void gameObjectAvailable(IGameObject gameObject) {
        // Do nothing
    }

    /**
     * Create the game object 2D.
     * @param gameObject the game object to create the game object 2D for
     * @return the game object 2D
     */
    protected abstract Actor createGameObject2d(final IGameObject gameObject);
}
