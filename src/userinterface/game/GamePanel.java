package userinterface.game;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JOptionPane;

import logger.Logger;
import misc.MyRandom;
import settings.Settings;
import simulation.IPlayer;
import simulation.Player;
import simulation.Region;
import simulation.item.ItemTypeManager;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.recipe.RecipeManager;
import simulation.workshop.WorkshopTypeManager;
import userinterface.misc.ImagePanel;
import userinterface.multiplayer.IMainWindow;
import controller.AbstractController;
import controller.ClientController;
import controller.Connection;
import controller.SinglePlayerController;

/**
 * The Game Panel.
 */
public class GamePanel extends ImagePanel implements IGamePanel {

    /** The serial version UID. */
    private static final long serialVersionUID = -3057695070550769148L;

    /** The main window. */
    private final IMainWindow mainWindow;

    /** The game loop runnable. */
    private GameLoop gameLoop;

    /** The controller. */
    private AbstractController controller;

    /** The region. */
    private Region region;

    /** The player. */
    private Player player;

    /** The world pane. */
    private WorldPane worldPane;

    /** The status bar. */
    private StatusBar statusPanel;

    /** The south panel. */
    private ManagementPanel managementPanel;

    /**
     * Constructor.
     * @param mainWindowTmp the main window
     */
    public GamePanel(final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        setupLayout();
    }

    /**
     * Start a single player game.
     * @param playerName the name of the player
     * @param regionSize the size of the region
     */
    public void startSinglePlayerGame(final String playerName, final MapIndex regionSize) {
        Logger.getInstance().log(this, "Starting single player game");
        try {
            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();
            MyRandom.getInstance().setSeed(10);

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            region = new Region();
            player = new Player(playerName);
            region.addPlayer(player);
            controller = new SinglePlayerController();

            managementPanel.setup(player, controller);
            worldPane.setup(region, player, controller, managementPanel);

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            player.setup(region, embarkPosition, numberOfStartingDwarfs);
            worldPane.getWorldCanvas().zoomToPosition(embarkPosition);

            gameLoop = new GameLoop(region, controller, this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not start the game");
            mainWindow.setupMainMenu();
        }
    }

    /**
     * Start a multiplayer game.
     * @param connection the connection to the server
     * @param playerNames the names of all the players
     * @param playerIndex the index of this player
     * @param regionSize the size of the region
     */
    public void startMultiplayerGame(final Connection connection, final List<String> playerNames,
            final int playerIndex, final MapIndex regionSize) {
        Logger.getInstance().log(this, "Starting multiplayer game");
        controller = new ClientController(connection, this);
        try {
            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();
            MyRandom.getInstance().setSeed(10);

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            region = new Region();
            for (String playerName : playerNames) {
                Player newPlayer = new Player(playerName);
                region.addPlayer(newPlayer);
                if (playerName.equals(playerNames.get(playerIndex))) {
                    player = newPlayer;
                }
            }
            controller = new ClientController(connection, this);

            managementPanel.setup(player, controller);
            worldPane.setup(region, player, controller, managementPanel);

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            for (IPlayer playerTmp : region.getPlayers()) {
                playerTmp.setup(region, embarkPosition, numberOfStartingDwarfs);
            }
            worldPane.getWorldCanvas().zoomToPosition(embarkPosition);

            gameLoop = new GameLoop(region, controller, this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not start the game");
            mainWindow.setupMainMenu();
        }
    }

    /**
     * Load a single player game from a file.
     */
    public void loadSinglePlayerGame() {
        Logger.getInstance().log(this, "Loading game");
        try {
            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();

            FileInputStream fileInputStream = new FileInputStream("myobject.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            region = (Region) objectInputStream.readObject();
            objectInputStream.close();

            // TODO: Which player, perhaps dialog...
            player = region.getPlayers().toArray(new Player[0])[0];
            controller = new SinglePlayerController();

            managementPanel.setup(player, controller);
            worldPane.setup(region, player, controller, managementPanel);

            gameLoop = new GameLoop(region, controller, this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not start the game");
            mainWindow.setupMainMenu();
        }
    }

    /**
     * Save single player game to a file.
     */
    public void saveSinglePlayerGame() {
        Logger.getInstance().log(this, "Saving game");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("myobject.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            // objectOutputStream.writeObject(region);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not save the game");
            mainWindow.setupMainMenu();
        }
    }

    @Override
    public void update() {
        worldPane.update();
        statusPanel.update(gameLoop, region, worldPane);
    }

    @Override
    public void disconnect() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        statusPanel = new StatusBar();
        add(statusPanel, BorderLayout.NORTH);

        worldPane = new WorldPane();
        add(worldPane, BorderLayout.CENTER);

        managementPanel = new ManagementPanel();
        add(managementPanel, BorderLayout.SOUTH);
    }
}
