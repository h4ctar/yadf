package yadf.userinterface.nifty.mainmenu;

import yadf.userinterface.nifty.RenderLoop;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainMenu implements ScreenController {

    private Nifty nifty;

    public MainMenu(RenderLoop renderLoop) {
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

    public void newSinglePlayerGame() {
        nifty.gotoScreen("new-single-player-game-menu");
    }

    public void loadSinglePlayerGame() {

    }

    public void joinMultiplayerGame() {

    }

    public void hostMultiplayerGame() {

    }

    public void quit() {
        nifty.exit();
    }
}
