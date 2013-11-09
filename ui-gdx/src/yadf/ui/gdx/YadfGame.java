package yadf.ui.gdx;

import java.util.Stack;

import yadf.simulation.item.ItemTypeManager;
import yadf.simulation.labor.LaborTypeManager;
import yadf.simulation.recipe.RecipeManager;
import yadf.simulation.workshop.WorkshopTypeManager;
import yadf.ui.gdx.screen.IScreenController;
import yadf.ui.gdx.screen.SplashScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * The yadf game class.
 */
public class YadfGame extends Game implements IScreenController {

    /** The current screen stack. */
    Stack<Screen> currentScreenStack = new Stack<>();

    @Override
    public void create() {
        addScreen(new SplashScreen(this));

        try {
            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(final int width, final int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void addScreen(final Screen screen) {
        System.out.println("addScreen: " + screen.getClass().getSimpleName());
        currentScreenStack.add(screen);
        setScreen(screen);
    }

    @Override
    public void replaceScreen(final Screen screen) {
        System.out.println("replaceScreen: " + screen.getClass().getSimpleName());
        currentScreenStack.pop();
        currentScreenStack.add(screen);
        setScreen(screen);
    }

    @Override
    public void endScreen() {
        System.out.println("endScreen");
        currentScreenStack.pop();
        if (!currentScreenStack.isEmpty()) {
            Screen screen = currentScreenStack.peek();
            setScreen(screen);
        } else {
            Gdx.app.exit();
        }
    }
}
