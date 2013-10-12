package yadf.ui.gdx;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import yadf.ui.gdx.screen.GameScreen;
import yadf.ui.gdx.screen.IScreenController;
import yadf.ui.gdx.screen.MainMenuScreen;
import yadf.ui.gdx.screen.SplashScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class YadfGame extends Game implements IScreenController {

    private Map<String, Screen> screens = new HashMap<>();

    Stack<Screen> currentScreenStack = new Stack<>();

    @Override
    public void create() {
        screens.put("splash", new SplashScreen(this));
        screens.put("main_menu", new MainMenuScreen(this));
        screens.put("game", new GameScreen(this));
        setScreen("splash");
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
    public void resize(int width, int height) {
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
    public void setScreen(String screenName) {
        System.out.println("setScreen: " + screenName);
        Screen screen = screens.get(screenName);
        currentScreenStack.add(screen);
        setScreen(screen);
    }

    @Override
    public void replaceScreen(String screenName) {
        System.out.println("replaceScreen: " + screenName);
        currentScreenStack.pop();
        setScreen(screenName);
    }

    @Override
    public void endScreen() {
        System.out.println("endScreen");
        currentScreenStack.pop();
        if (!currentScreenStack.isEmpty()) {
            setScreen(currentScreenStack.peek());
        } else {
            Gdx.app.exit();
        }
    }
}
