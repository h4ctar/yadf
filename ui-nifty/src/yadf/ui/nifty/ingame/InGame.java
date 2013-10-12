package yadf.ui.nifty.ingame;

import yadf.ui.nifty.RenderLoop;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class InGame implements ScreenController {

    private Nifty nifty;

    private RenderLoop renderLoop;

    private InGameRenderCallback renderCallback;

    public InGame(RenderLoop renderLoopTmp) {
        renderLoop = renderLoopTmp;
        renderCallback = new InGameRenderCallback();
    }

    @Override
    public void bind(Nifty niftyTmp, Screen screen) {
        nifty = niftyTmp;
    }

    @Override
    public void onStartScreen() {
        renderLoop.setRenderCallback(renderCallback);
        renderCallback.init();
    }

    @Override
    public void onEndScreen() {
        renderCallback.deinit();
        renderLoop.setRenderCallback(null);
    }
}
