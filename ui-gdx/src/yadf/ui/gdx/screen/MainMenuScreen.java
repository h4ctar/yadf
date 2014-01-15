package yadf.ui.gdx.screen;

import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * Constructor.
     * @param screenController the screen controller.
     */
    public MainMenuScreen(final IScreenController screenController) {
        super(screenController);
    }

    @Override
    public void show() {
        super.show();
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);

        TextButton newSinglePlayerGameButton = new TextButton("New Single Player Game", skin);
        newSinglePlayerGameButton.addListener(new NewSinglePlayerGameButtonListener());
        table.add(newSinglePlayerGameButton).spaceBottom(10);

        table.row();
        TextButton loadSinglePlayerGameButton = new TextButton("Load Single Player Game", skin);
        loadSinglePlayerGameButton.addListener(new LoadSinglePlayerGameButtonListener());
        table.add(loadSinglePlayerGameButton).spaceBottom(10);

        table.row();
        TextButton hostMultiplayerGameButton = new TextButton("Host Multiplayer Game", skin);
        hostMultiplayerGameButton.addListener(new HostMultiplayerGameButtonListener());
        table.add(hostMultiplayerGameButton).spaceBottom(10);

        table.row();
        TextButton joinMultiplayerGameButton = new TextButton("Join Multiplayer Game", skin);
        joinMultiplayerGameButton.addListener(new JoinMultiplayerGameButtonListener());
        table.add(joinMultiplayerGameButton).spaceBottom(10);

        table.row();
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new QuitButtonListener());
        table.add(quitButton);

        uiStage.addActor(table);
    }

    /**
     * The listener for the new single player game button.
     */
    private final class NewSinglePlayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            screenController.replaceScreen(new GameScreen(screenController));
        }
    }

    /**
     * The listener for the load single player game button.
     */
    private final class LoadSinglePlayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
        }
    }

    /**
     * The listener for the host multiplayer game button.
     */
    private final class HostMultiplayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
        }
    }

    /**
     * The listener for the join multiplayer game button.
     */
    private final class JoinMultiplayerGameButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
        }
    }

    /**
     * The listener for the quit button.
     */
    private final class QuitButtonListener extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            screenController.endScreen();
        }
    }
}
