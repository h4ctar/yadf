package yadf.ui.gdx.screen.game;

import com.badlogic.gdx.InputProcessor;

public interface IInteractor {

    InputProcessor getInputProcessor();

    void start();

    void addListener(IInteractorListener interactorListener);

    void removeListener(IInteractorListener interactorListener);
}
