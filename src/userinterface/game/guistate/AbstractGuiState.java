package userinterface.game.guistate;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGuiState implements IGuiState {

    private Set<IGuiStateListener> listeners = new HashSet<>();

    @Override
    public void addGuiStateListener(final IGuiStateListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGuiStateListener(final IGuiStateListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    protected void notifyListeners() {
        for (IGuiStateListener listener : listeners) {
            listener.stateDone();
        }
    }
}
