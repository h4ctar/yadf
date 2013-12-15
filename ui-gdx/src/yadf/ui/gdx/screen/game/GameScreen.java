package yadf.ui.gdx.screen.game;

import java.util.Stack;

import yadf.controller.AbstractController;
import yadf.controller.SinglePlayerController;
import yadf.misc.MyRandom;
import yadf.settings.Settings;
import yadf.simulation.GoblinPlayer;
import yadf.simulation.HumanPlayer;
import yadf.simulation.Region;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.item.IStockManager;
import yadf.simulation.map.MapIndex;
import yadf.simulation.workshop.IWorkshopManager;
import yadf.ui.gdx.screen.AbstractScreen;
import yadf.ui.gdx.screen.IScreenController;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.dialogwindow.IDialogWindowManager;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;
import yadf.ui.gdx.screen.game.object.GameCharacter2dController;
import yadf.ui.gdx.screen.game.object.Item2dController;
import yadf.ui.gdx.screen.game.object.Plant2dController;
import yadf.ui.gdx.screen.game.object.Workshop2dController;
import yadf.ui.gdx.screen.game.toolbar.IToolbarManager;
import yadf.ui.gdx.screen.game.toolbar.MainToolbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The game screen.
 */
public class GameScreen extends AbstractScreen implements IToolbarManager, IInteractorManager, IDialogWindowManager {

    /** The size of a sprite. */
    public static final int SPRITE_SIZE = 18;

    /** The number of simulation steps to take before sending commands. */
    private static final int SIMULATION_STEPS_SEND_COMMAND = 10;

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    /** The map renderer. */
    private MapRenderer mapRenderer;

    /** The region. */
    private Region region;

    /** The human player. */
    private HumanPlayer player;

    /** The controller. */
    private AbstractController controller;

    /** How many simulation steps since the controller was updated. */
    private int simulationSteps;

    /** The camera. */
    private TileCamera camera = new TileCamera();

    /** The input processor for the camera. */
    private CameraInputProcessor cameraInputProcessor;

    /** The stage for the game objects. */
    private Stage gameStage;

    /** The controls stack (the buttons in the top left). */
    private Stack<Actor> toolbarStack = new Stack<>();

    /** The current dialog window. */
    private Dialog dialogWindow = null;

    /** The current interactor. */
    private IInteractor interactor;

    /**
     * Constructor.
     * @param screenController the screen controller.
     */
    public GameScreen(final IScreenController screenController) {
        super(screenController);
    }

    @Override
    public void show() {
        super.show();

        gameStage = new Stage();
        textureAtlas = new TextureAtlas(Gdx.files.internal("image-atlases/images.atlas"));
        setupSinglePlayerGame();

        mapRenderer = new MapRenderer(region.getMap(), textureAtlas);
        cameraInputProcessor = new CameraInputProcessor(camera);
        inputMultiplexer.addProcessor(cameraInputProcessor);
        gameStage.setCamera(camera);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        toolbarStack.add(new MainToolbar(skin, this, this, this, player, camera, controller));
        uiStage.addActor(toolbarStack.peek());
    }

    /**
     * Setup a new single player game.
     */
    private void setupSinglePlayerGame() {
        try {
            MyRandom.getInstance().setSeed(2);
            MapIndex regionSize = new MapIndex(128, 128, 16);
            String playerName = "Ben";

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            region = new Region();

            player = new HumanPlayer(playerName, region);

            // TODO: do this later and make method to get all game objects
            player.getComponent(ICharacterManager.class).addManagerListener(
                    new GameCharacter2dController(textureAtlas, gameStage));
            player.getComponent(IStockManager.class).getUnstoredItemManager()
                    .addManagerListener(new Item2dController(textureAtlas, gameStage));
            player.getComponent(IWorkshopManager.class).addManagerListener(
                    new Workshop2dController(textureAtlas, gameStage));
            // player.getComponent(IJobManager.class).addGameObjectManagerListener(new Job2dController(gameStage));
            region.getTreeManager().addManagerListener(new Plant2dController(textureAtlas, gameStage));

            region.addPlayer(player);

            GoblinPlayer goblinPlayer = new GoblinPlayer(region);
            region.addPlayer(goblinPlayer);

            controller = new SinglePlayerController();

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            player.setup(embarkPosition, numberOfStartingDwarfs);
            goblinPlayer.setup();

            camera.zoomToPosition(embarkPosition);
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(final int width, final int height) {
        super.resize(width, height);
        camera.resize(width, height);
    }

    @Override
    protected void update(final float delta) {
        super.update(delta);
        gameStage.act();
        try {
            if (simulationSteps++ > SIMULATION_STEPS_SEND_COMMAND) {
                controller.doCommands(region);
                simulationSteps = 0;
            }
            region.update();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    protected void draw() {
        mapRenderer.draw(camera);
        gameStage.draw();
        if (interactor != null) {
            interactor.draw();
        }
    }

    @Override
    public void setToolbar(final Actor controls) {
        uiStage.getRoot().removeActor(toolbarStack.peek());
        toolbarStack.add(controls);
        uiStage.addActor(controls);
    }

    @Override
    public void closeToolbar() {
        uiStage.getRoot().removeActor(toolbarStack.pop());
        if (!toolbarStack.isEmpty()) {
            Actor controls = toolbarStack.peek();
            uiStage.addActor(controls);
        }
    }

    @Override
    public void setDialogWindow(final Dialog dialogWindowTmp) {
        if (dialogWindow != null) {
            dialogWindow.hide();
        }
        dialogWindow = dialogWindowTmp;
        dialogWindow.show(uiStage);
    }

    @Override
    public void closeDialogWindow(final Dialog dialogWindowTmp) {
        assert dialogWindow == dialogWindowTmp;
        dialogWindow.hide();
        dialogWindow = null;
    }

    @Override
    public void installInteractor(final IInteractor interactorTmp) {
        assert interactor == null;
        interactor = interactorTmp;
        interactor.addListener(this);
        inputMultiplexer.addProcessor(0, interactor.getInputProcessor());
        interactor.start();
    }

    @Override
    public void interactionDone(final IInteractor interactorTmp) {
        assert interactor == interactorTmp;
        inputMultiplexer.removeProcessor(interactor.getInputProcessor());
        interactor = null;
    }
}
