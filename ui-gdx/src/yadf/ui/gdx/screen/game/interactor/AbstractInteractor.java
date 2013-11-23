package yadf.ui.gdx.screen.game.interactor;

import java.util.HashSet;
import java.util.Set;

/**
 * An abstract interactor.
 * <p>
 * Provides the basic listener implementation stuffs.
 */
public abstract class AbstractInteractor implements IInteractor {

    /** The listeners. */
    private Set<IInteractorListener> listeners = new HashSet<>();

    @Override
    public void addListener(final IInteractorListener interactorListener) {
        assert !listeners.contains(interactorListener);
        listeners.add(interactorListener);
    }

    @Override
    public void removeListener(final IInteractorListener interactorListener) {
        assert listeners.contains(interactorListener);
        listeners.remove(interactorListener);
    }

    /**
     * Notify all the listeners that this interactor is finished.
     */
    protected void finishInteraction() {
        for (IInteractorListener listener : listeners) {
            listener.interactionDone(this);
        }
    }
}
