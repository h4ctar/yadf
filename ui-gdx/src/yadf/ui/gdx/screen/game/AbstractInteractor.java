package yadf.ui.gdx.screen.game;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractInteractor implements IInteractor {

    private Set<IInteractorListener> listeners = new HashSet<>();

    @Override
    public void addListener(IInteractorListener interactorListener) {
        assert !listeners.contains(interactorListener);
        listeners.add(interactorListener);
    }

    @Override
    public void removeListener(IInteractorListener interactorListener) {
        assert listeners.contains(interactorListener);
        listeners.remove(interactorListener);
    }

    protected void finishInteraction() {
        for (IInteractorListener listener : listeners) {
            listener.interactionDone(this);
        }
    }
}
