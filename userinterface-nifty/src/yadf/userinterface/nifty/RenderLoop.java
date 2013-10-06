package yadf.userinterface.nifty;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.lessvoid.nifty.Nifty;

public class RenderLoop {

    private Nifty nifty;

    private IRenderCallback renderCallback;

    public RenderLoop(Nifty niftyTmp) {
        nifty = niftyTmp;
    }

    public void setRenderCallback(IRenderCallback renderCallbackTmp) {
        renderCallback = renderCallbackTmp;
    }

    public void runLoop() {
        boolean done = false;
        while (!Display.isCloseRequested() && !done) {
            if (renderCallback != null) {
                renderCallback.process();
            }

            // show render
            Display.update();

            if (nifty.update()) {
                done = true;
            }

            nifty.render(false);

            // check gl error at least ones per frame
            int error = GL11.glGetError();
            if (error != GL11.GL_NO_ERROR) {
                String glerrmsg = GLU.gluErrorString(error);
                System.out.println("OpenGL Error: (" + error + ") " + glerrmsg);
                done = true;
            }
        }
    }
}