package yadf.ui.gdx.screen.game;

import java.util.HashMap;
import java.util.Map;

import yadf.controller.AbstractController;
import yadf.controller.SinglePlayerController;
import yadf.misc.MyRandom;
import yadf.settings.Settings;
import yadf.simulation.GoblinPlayer;
import yadf.simulation.HumanPlayer;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.Region;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.AbstractScreen;
import yadf.ui.gdx.screen.IScreenController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The game screen.
 */
public class GameScreen extends AbstractScreen implements IGameObjectManagerListener {

    /** The size of a sprite. */
    public static final int SPRITE_SIZE = 18;

    /** The number of simulation steps to take before sending commands. */
    private static final int SIMULATION_STEPS_SEND_COMMAND = 10;

    /** The sprite batch. */
    private SpriteBatch spriteBatch;

    /** The texture atlas. */
    private TextureAtlas atlas;

    /** The map renderer. */
    private MapRenderer mapRenderer;

    /** The region. */
    private Region region;

    /** The human player. */
    private HumanPlayer player;

    /** The controller. */
    private AbstractController controller;

    /** The currently viewable area. */
    private MapArea viewArea = new MapArea();

    /** How many simulation steps since the controller was updated. */
    private int simulationSteps;

    private Map<IGameObject, Actor> gameObject2ds = new HashMap<>();

    /**
     * Constructor.
     * @param screenController the screen controller.
     */
    public GameScreen(IScreenController screenController) {
        super(screenController);
    }

    @Override
    public void show() {
        super.show();

        spriteBatch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("image-atlases/images.atlas"));

        setupSinglePlayerGame();

        mapRenderer = new MapRenderer(region.getMap(), atlas, spriteBatch);
    }

    /**
     * Setup a new single player game.
     */
    private void setupSinglePlayerGame() {
        try {
            MyRandom.getInstance().setSeed(2);
            MapIndex regionSize = new MapIndex(64, 64, 32);
            String playerName = "Ben";

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            region = new Region();

            player = new HumanPlayer(playerName, region);
            player.getComponent(ICharacterManager.class).addGameObjectManagerListener(this);
            region.addPlayer(player);

            GoblinPlayer goblinPlayer = new GoblinPlayer(region);
            region.addPlayer(goblinPlayer);

            controller = new SinglePlayerController();

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            player.setup(embarkPosition, numberOfStartingDwarfs);
            goblinPlayer.setup();
            zoomToPosition(embarkPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zoom to a map index.
     * @param position the position to zoom to
     */
    private void zoomToPosition(MapIndex position) {
        viewArea.pos.x = position.x - viewArea.width / 2;
        viewArea.pos.y = position.y - viewArea.height / 2;
        viewArea.pos.z = position.z;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        // To keep the view centered on the same location it is moved so the corner is where it use to be centered then
        // moved back or something, does that make any sense?
        viewArea.pos.x += viewArea.width / 2;
        viewArea.pos.y += viewArea.height / 2;
        viewArea.width = width / SPRITE_SIZE + 1;
        viewArea.height = height / SPRITE_SIZE + 1;
        viewArea.pos.x -= viewArea.width / 2;
        viewArea.pos.y -= viewArea.height / 2;
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        try {
            if (simulationSteps++ > SIMULATION_STEPS_SEND_COMMAND) {
                controller.doCommands(region);
                simulationSteps = 0;
            }
            region.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void drawBackground() {
        spriteBatch.begin();
        mapRenderer.render(viewArea);
        spriteBatch.end();
    }

    @Override
    public void gameObjectAdded(IGameObject gameObject, int index) {
        assert !gameObject2ds.containsKey(gameObject);
        Actor gameObject2d = new GameCharacter2d((IGameCharacter) gameObject, atlas);
        gameObject2ds.put(gameObject, gameObject2d);
        stage.addActor(gameObject2d);
    }

    @Override
    public void gameObjectRemoved(IGameObject gameObject, int index) {
        assert gameObject2ds.containsKey(gameObject);
        Actor gameObject2d = gameObject2ds.remove(gameObject);
        stage.getRoot().removeActor(gameObject2d);
    }
}
