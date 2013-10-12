package yadf.ui.nifty.mainmenu;

import yadf.ui.nifty.RenderLoop;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NewSinglePlayerGameMenu implements ScreenController {

    private Nifty nifty;

    public NewSinglePlayerGameMenu(RenderLoop renderLoop) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void bind(Nifty niftyTmp, Screen screen) {
        nifty = niftyTmp;
    }

    @Override
    public void onStartScreen() {
        // do nothing
    }

    @Override
    public void onEndScreen() {
        // do nothing
    }

    public void startGame() {
        nifty.gotoScreen("in-game");
    }

    public void back() {
        nifty.gotoScreen("main-menu");
    }
}
