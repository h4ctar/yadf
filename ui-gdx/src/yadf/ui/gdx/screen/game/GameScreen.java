package yadf.ui.gdx.screen.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import yadf.controller.AbstractController;
import yadf.controller.SinglePlayerController;
import yadf.misc.MyRandom;
import yadf.settings.Settings;
import yadf.simulation.GoblinPlayer;
import yadf.simulation.HumanPlayer;
import yadf.simulation.IGameObject;
import yadf.simulation.Region;
import yadf.simulation.Tree;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.item.Stockpile;
import yadf.simulation.map.MapIndex;
import yadf.simulation.room.IRoomManager;
import yadf.simulation.room.Room;
import yadf.simulation.workshop.IWorkshop;
import yadf.simulation.workshop.IWorkshopManager;
import yadf.ui.gdx.screen.AbstractScreen;
import yadf.ui.gdx.screen.IScreenController;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.IInteractor;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;
import yadf.ui.gdx.screen.game.toolbar.IToolbarManager;
import yadf.ui.gdx.screen.game.toolbar.MainToolbar;
import yadf.ui.gdx.screen.game.view.CharacterViewController;
import yadf.ui.gdx.screen.game.view.IViewController;
import yadf.ui.gdx.screen.game.view.ItemViewController;
import yadf.ui.gdx.screen.game.view.PlantViewController;
import yadf.ui.gdx.screen.game.view.RoomViewController;
import yadf.ui.gdx.screen.game.view.StockpileViewController;
import yadf.ui.gdx.screen.game.view.WorkshopViewController;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

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
    private HumanPlayer thisPlayer;

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
    private Window window = null;

    /** The current interactor. */
    private IInteractor interactor;

    /** The skin for everything. */
    private Skin skin;

    /** The view controllers. */
    private Map<Class<? extends IGameObject>, IViewController<?>> viewControllers = new HashMap<>();

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
        inputMultiplexer.addProcessor(gameStage);
        textureAtlas = new TextureAtlas(Gdx.files.internal("image-atlases/images.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        cameraInputProcessor = new CameraInputProcessor(camera);
        inputMultiplexer.addProcessor(cameraInputProcessor);
        gameStage.setCamera(camera);
        createViewControllers();
        setupSinglePlayerGame();
        mapRenderer = new MapRenderer(region.getMap(), textureAtlas);
        setToolbar(new MainToolbar(this, this, this, thisPlayer, camera, controller));
    }

    /**
     * Setup a new single player game.
     */
    private void setupSinglePlayerGame() {
        MyRandom.getInstance().setSeed(2);

        region = new Region();
        MapIndex regionSize = new MapIndex(128, 128, 16);
        region.setup(regionSize);
        setupViewControllers(region);

        String playerName = "Ben";
        thisPlayer = new HumanPlayer(playerName, region);
        region.addPlayer(thisPlayer);
        MapIndex embarkPosition = region.getMap().getRandomSurfacePosition();
        embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
        int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
        thisPlayer.setup(embarkPosition, numberOfStartingDwarfs);
        setupViewControllers(thisPlayer);

        GoblinPlayer goblinPlayer = new GoblinPlayer(region);
        region.addPlayer(goblinPlayer);
        goblinPlayer.setup();
        setupViewControllers(goblinPlayer);

        controller = new SinglePlayerController();

        camera.zoomToPosition(embarkPosition);
    }

    private <T extends IGameObject> void setViewController(Class<T> clazz, IViewController<T> viewController) {
        viewControllers.put(clazz, viewController);
    }

    @SuppressWarnings("unchecked")
    private <T extends IGameObject> IViewController<T> getViewController(Class<T> clazz) {
        return (IViewController<T>) viewControllers.get(clazz);
    }

    private void createViewControllers() {
        setViewController(IGameCharacter.class, new CharacterViewController(textureAtlas, gameStage));
        setViewController(Item.class, new ItemViewController(textureAtlas, gameStage));
        setViewController(Stockpile.class, new StockpileViewController(textureAtlas, gameStage, getViewController(Item.class), this));
        setViewController(IWorkshop.class, new WorkshopViewController(textureAtlas, gameStage, thisPlayer, controller, this));
        setViewController(Room.class, new RoomViewController(textureAtlas, gameStage, this));
        setViewController(Tree.class, new PlantViewController(textureAtlas, gameStage));
    }

    private void setupViewControllers(Region region) {
        getViewController(Tree.class).addManager(region.getTreeManager());
    }

    private void setupViewControllers(HumanPlayer player) {
        getViewController(IGameCharacter.class).addManager(player.getComponent(ICharacterManager.class));
        getViewController(Item.class).addManager(player.getComponent(IStockManager.class).getUnstoredItemManager());
        getViewController(Stockpile.class).addManager(player.getComponent(IStockManager.class).getStockpileManager());
        getViewController(IWorkshop.class).addManager(player.getComponent(IWorkshopManager.class));
        getViewController(Room.class).addManager(player.getComponent(IRoomManager.class));
    }

    private void setupViewControllers(GoblinPlayer player) {
        getViewController(IGameCharacter.class).addManager(player.getComponent(ICharacterManager.class));
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
        if (!toolbarStack.isEmpty()) {
            uiStage.getRoot().removeActor(toolbarStack.peek());
        }
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
    public void setWindow(final Window windowTmp) {
        assert window != windowTmp;
        if (window != null) {
            window.remove();
        }
        window = windowTmp;
        uiStage.addActor(window);
    }

    @Override
    public void closeWindow(final Window windowTmp) {
        assert window == windowTmp;
        window.remove();
        window = null;
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

    @Override
    public Skin getSkin() {
        return skin;
    }
}
