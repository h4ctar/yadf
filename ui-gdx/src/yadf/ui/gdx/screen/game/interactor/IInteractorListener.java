package yadf.ui.gdx.screen.game.interactor;

/**
 * Interface for an interactor listener.
 */
public interface IInteractorListener {

    /**
     * The interactor is finished.
     * @param interactor the interactor that is finished
     */
    void interactionDone(IInteractor interactor);
}
