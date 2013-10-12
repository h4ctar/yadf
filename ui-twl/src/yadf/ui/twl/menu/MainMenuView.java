package yadf.ui.twl.menu;

import yadf.ui.twl.MainView;
import yadf.ui.twl.Window;
import de.matthiasmann.twl.BoxLayout;
import de.matthiasmann.twl.BoxLayout.Direction;
import de.matthiasmann.twl.Button;

public class MainMenuView extends MainView {

    private Window window;

    private Button newSinglePlayerGameButton;

    private Button loadSinglePlayerGameButton;

    private Button joinMultiplayerGameButton;

    private Button hostMultiplayerGameButton;

    private Button quitButton;

    public MainMenuView(Window windowTmp) {
        window = windowTmp;

        newSinglePlayerGameButton = new Button("New Single Player Game");
        newSinglePlayerGameButton.setTheme("button");
        newSinglePlayerGameButton.addCallback(new NewGameCallback());

        loadSinglePlayerGameButton = new Button("Load Single Player Game");
        loadSinglePlayerGameButton.setTheme("button");

        joinMultiplayerGameButton = new Button("Join Multiplayer Game");
        joinMultiplayerGameButton.setTheme("button");

        hostMultiplayerGameButton = new Button("Host Multiplayer Game");
        hostMultiplayerGameButton.setTheme("button");

        quitButton = new Button("Quit");
        quitButton.setTheme("button");

        BoxLayout buttonLayout = new BoxLayout(Direction.VERTICAL);

        buttonLayout.add(newSinglePlayerGameButton);
        buttonLayout.add(loadSinglePlayerGameButton);
        buttonLayout.add(joinMultiplayerGameButton);
        buttonLayout.add(hostMultiplayerGameButton);
        buttonLayout.add(quitButton);

        add(buttonLayout);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void update() {
    }

    private class NewGameCallback implements Runnable {

        @Override
        public void run() {
            window.setView("game");
        }
    }
}
