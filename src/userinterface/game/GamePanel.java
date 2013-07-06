package userinterface.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import logger.Logger;
import misc.MyRandom;
import settings.Settings;
import simulation.IPlayer;
import simulation.Player;
import simulation.Region;
import simulation.map.MapIndex;
import userinterface.game.guistate.IGuiState;
import userinterface.game.guistate.IGuiStateListener;
import userinterface.game.guistate.NormalGuiState;
import userinterface.misc.ImagePanel;
import userinterface.multiplayer.IMainWindow;
import controller.AbstractController;
import controller.ClientController;
import controller.Connection;
import controller.SinglePlayerController;

/**
 * The Game Panel.
 */
public class GamePanel extends ImagePanel implements IGamePanel, IGuiStateListener {

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
    private WorldPanel worldPanel;

    /** The status bar. */
    private StatusBar statusPanel;

    /** The south panel. */
    private ManagementPanel managementPanel;

    /** The current state the the GUI is in. */
    private IGuiState state;

    /**
     * Constructor.
     * @param mainWindowTmp the main window
     */
    public GamePanel(final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        setupLayout();
        setupKeyboardActions();
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        statusPanel = new StatusBar();
        add(statusPanel, BorderLayout.NORTH);

        worldPanel = new WorldPanel();
        add(worldPanel, BorderLayout.CENTER);
        worldPanel.setSize(new Dimension(400, 400));

        managementPanel = new ManagementPanel();
        add(managementPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup all the keyboard actions.
     */
    private void setupKeyboardActions() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true),
                "SHIFT_UP");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "UP");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "DOWN");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "LEFT");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RIGHT");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "UP_Z");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "DOWN_Z");

        getActionMap().put("SHIFT_UP", new ShiftAction());
        getActionMap().put("UP", new MoveViewAction(0, -1, 0));
        getActionMap().put("DOWN", new MoveViewAction(0, 1, 0));
        getActionMap().put("LEFT", new MoveViewAction(-1, 0, 0));
        getActionMap().put("RIGHT", new MoveViewAction(1, 0, 0));
        getActionMap().put("UP_Z", new MoveViewAction(0, 0, 1));
        getActionMap().put("DOWN_Z", new MoveViewAction(0, 0, -1));
    }

    /**
     * Start a single player game.
     * @param playerName the name of the player
     * @param regionSize the size of the region
     */
    public void startSinglePlayerGame(final String playerName, final MapIndex regionSize) {
        Logger.getInstance().log(this, "Starting single player game");
        try {
            MyRandom.getInstance().setSeed(2);

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            region = new Region();
            player = new Player(playerName);
            region.addPlayer(player);
            controller = new SinglePlayerController();

            worldPanel.setup(player, region);
            managementPanel.setup(player, controller, this);

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            player.setup(region, embarkPosition, numberOfStartingDwarfs);
            worldPanel.zoomToPosition(embarkPosition);

            gameLoop = new GameLoop(region, controller, this);

            state = new NormalGuiState();
            state.setup(player, controller, this);
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
            MyRandom.getInstance().setSeed(2);

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

            worldPanel.setup(player, region);
            managementPanel.setup(player, controller, this);

            region.setup(regionSize);
            embarkPosition.z = region.getMap().getHeight(embarkPosition.x, embarkPosition.y);
            for (IPlayer playerTmp : region.getPlayers()) {
                playerTmp.setup(region, embarkPosition, numberOfStartingDwarfs);
            }
            worldPanel.zoomToPosition(embarkPosition);

            gameLoop = new GameLoop(region, controller, this);

            state = new NormalGuiState();
            state.setup(player, controller, this);
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
            MyRandom.getInstance().setSeed(2);

            FileInputStream fileInputStream = new FileInputStream("myobject.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            region = (Region) objectInputStream.readObject();
            objectInputStream.close();

            player = region.getPlayers().toArray(new Player[0])[0];
            controller = new SinglePlayerController();

            worldPanel.setup(player, region);
            managementPanel.setup(player, controller, this);

            gameLoop = new GameLoop(region, controller, this);

            state = new NormalGuiState();
            state.setup(player, controller, this);
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
        worldPanel.repaint();
        statusPanel.update(gameLoop, region, state == null ? "Normal" : state.toString());
    }

    @Override
    public void disconnect() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    @Override
    public void setState(final IGuiState stateTmp) {
        state.interrupt();
        state = stateTmp;
        state.addGuiStateListener(this);
        state.setup(player, controller, this);
    }

    @Override
    public void stateDone() {
        state.removeGuiStateListener(this);
        state = new NormalGuiState();
        state.setup(player, controller, this);
    }

    /**
     * This action should interrupt the state when the shift key is released.
     */
    private class ShiftAction extends AbstractAction {

        /** The serial version UID. */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!(state instanceof NormalGuiState)) {
                state.interrupt();
            }
        }
    }

    /**
     * The action to move the view.
     */
    private class MoveViewAction extends AbstractAction {

        /** The serial version UID. */
        private static final long serialVersionUID = 1L;

        /** How far to move in x direction. */
        private int x;

        /** How far to move in y direction. */
        private int y;

        /** How far to move in y direction. */
        private int z;

        /**
         * Constructor.
         * @param xTmp how far to move in x direction
         * @param yTmp how far to move in y direction
         * @param zTmp how far to move in z direction
         */
        public MoveViewAction(final int xTmp, final int yTmp, final int zTmp) {
            x = xTmp;
            y = yTmp;
            z = zTmp;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            worldPanel.moveView(x, y, z);
        }
    }

    @Override
    public WorldPanel getWorldPanel() {
        return worldPanel;
    }

    @Override
    public ManagementPanel getManagementPanel() {
        return managementPanel;
    }
}
