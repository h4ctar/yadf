package yadf.ui.gdx.screen;

import com.badlogic.gdx.Screen;

/**
 * Interface for a screen controller.
 */
public interface IScreenController {

    /**
     * Add a new screen to the stack.
     * @param screen the screen to add
     */
    void addScreen(Screen screen);

    /**
     * Replace the current screen with another.
     * @param screen the new screen
     */
    void replaceScreen(Screen screen);

    /**
     * End the current screen.
     */
    void endScreen();
}
