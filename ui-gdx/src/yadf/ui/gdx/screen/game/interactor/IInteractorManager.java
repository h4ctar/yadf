package yadf.ui.gdx.screen.game.interactor;

/**
 * Interface for an interactor manager.
 */
public interface IInteractorManager extends IInteractorListener {

    /**
     * Install an interactor.
     * @param interactor the interactor to install
     */
    void installInteractor(IInteractor interactor);
}
