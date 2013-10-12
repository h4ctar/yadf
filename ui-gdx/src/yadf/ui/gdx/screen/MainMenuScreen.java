package yadf.ui.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(IScreenController screenController) {
        super(screenController);
    }

    @Override
    public void show() {
        super.show();
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton newSinglePlayerGameButton = new TextButton("New Single Player Game", skin);
        newSinglePlayerGameButton.addListener(new NewSinglePlayerGameButtonListener());
        table.add(newSinglePlayerGameButton).width(300).spaceBottom(10);

        table.row();
        TextButton loadSinglePlayerGameButton = new TextButton("Load Single Player Game", skin);
        loadSinglePlayerGameButton.addListener(new LoadSinglePlayerGameButtonListener());
        table.add(loadSinglePlayerGameButton).width(300).spaceBottom(10);

        table.row();
        TextButton hostMultiplayerGameButton = new TextButton("Host Multiplayer Game", skin);
        hostMultiplayerGameButton.addListener(new HostMultiplayerGameButtonListener());
        table.add(hostMultiplayerGameButton).width(300).spaceBottom(10);

        table.row();
        TextButton joinMultiplayerGameButton = new TextButton("Join Multiplayer Game", skin);
        joinMultiplayerGameButton.addListener(new JoinMultiplayerGameButtonListener());
        table.add(joinMultiplayerGameButton).width(300).spaceBottom(10);

        table.row();
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new QuitButtonListener());
        table.add(quitButton).width(300);
    }

    private final class NewSinglePlayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            screenController.setScreen("game");
        }
    }

    private final class LoadSinglePlayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
        }
    }

    private final class HostMultiplayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
        }
    }

    private final class JoinMultiplayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
        }
    }

    private final class QuitButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            screenController.endScreen();
        }
    }
}
