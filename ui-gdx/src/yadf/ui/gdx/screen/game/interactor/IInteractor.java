package yadf.ui.gdx.screen.game.interactor;

import com.badlogic.gdx.InputProcessor;

/**
 * Interface for interactors to implement.
 */
public interface IInteractor {

    /**
     * Get the input processor for the interactor.
     * @return the input processor
     */
    InputProcessor getInputProcessor();

    /**
     * Start the interactor.
     */
    void start();

    /**
     * Add a listener to the interactor that will be notified when the interactor is done.
     * @param interactorListener the listener to add
     */
    void addListener(IInteractorListener interactorListener);

    /**
     * Remove a listener from the interactor.
     * @param interactorListener the listener to remove
     */
    void removeListener(IInteractorListener interactorListener);

    /**
     * Draw the interactor.
     */
    void draw();
}
