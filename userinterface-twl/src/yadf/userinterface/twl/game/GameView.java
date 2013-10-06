package yadf.userinterface.twl.game;

import org.lwjgl.opengl.GL11;

import yadf.userinterface.twl.MainView;
import yadf.userinterface.twl.Window;

public class GameView extends MainView {

    public GameView(Window windowTmp) {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void update() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(100, 100);
        GL11.glVertex2f(100 + 200, 100);
        GL11.glVertex2f(100 + 200, 100 + 200);
        GL11.glVertex2f(100, 100 + 200);
        GL11.glEnd();
    }
}
