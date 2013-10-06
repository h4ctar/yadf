package yadf.userinterface.twl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import yadf.userinterface.twl.game.GameView;
import yadf.userinterface.twl.menu.MainMenuView;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Window extends Container implements IWindow {

    private static final int WIDTH = 1440;

    private static final int HEIGHT = 810;

    private GUI gui;

    private LWJGLRenderer renderer;

    private ThemeManager themeManager;

    private Map<String, MainView> views;

    private MainView currentView;

    public Window() {
        setupLwjgl();
        setupOpenGl();
        initTwl();

        views = new HashMap<>();
        views.put("mainmenu", new MainMenuView(this));
        views.put("game", new GameView(this));
        setView("mainmenu");
    }

    private void setupLwjgl() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("Ben's Burrows");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private void setupOpenGl() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glOrtho(0.0f, WIDTH, 0.0f, HEIGHT, 1.0f, -1.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
    }

    private void initTwl() {
        try {
            renderer = new LWJGLRenderer();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        gui = new GUI(this, renderer);

        try {
            themeManager = ThemeManager.createThemeManager(getClass().getResource("/theme.xml"), renderer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gui.applyTheme(themeManager);
    }

    public void start() {
        while (!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            if (currentView != null) {
                currentView.update();
            }
            gui.update();
            Display.update();
        }
    }

    public void setView(String viewName) {
        if (currentView != null) {
            boolean wasRemoved = removeChild(currentView);
            assert wasRemoved;
            currentView.stop();
        }
        currentView = views.get(viewName);
        assert currentView != null;
        currentView.start();
        add(currentView);
    }
}
