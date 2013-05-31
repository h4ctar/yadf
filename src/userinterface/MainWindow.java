/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package userinterface;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import logger.Logger;
import settings.Settings;
import simulation.Player;
import simulation.Region;
import simulation.item.ItemTypeManager;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.recipe.RecipeManager;
import simulation.workshop.WorkshopTypeManager;
import userinterface.components.ImagePanel;
import userinterface.job.JobsPane;
import userinterface.labor.LaborsPane;
import userinterface.lobby.IMainWindow;
import userinterface.lobby.LobbyPanel;
import userinterface.lobby.LobbyType;
import userinterface.server.DedicatedServerPanel;
import userinterface.stock.StocksPane;
import controller.AbstractController;
import controller.ClientController;
import controller.IControllerListener;
import controller.SinglePlayerController;

/**
 * The Class MainWindow.
 */
public class MainWindow extends JFrame implements IMainWindow, IControllerListener {

    /**
     * The Class ClientRunnable.
     */
    private class ClientRunnable implements Runnable {
        /** How many ms in one second. */
        private static final long MS_IN_A_SECOND = 1000;

        /** The desired period in ms. */
        private static final long DESIRED_PERIOD = 33;

        /** The number of simulation steps to take before sending commands. */
        private static final long SIMULATION_STEPS_SEND_COMMAND = 10;

        /** The thread. */
        private final Thread thread;

        /** The running. */
        private boolean running = true;

        /** The number of frames rendered in the last second. */
        private long frameCount;

        /**
         * Instantiates a new client runnable.
         */
        public ClientRunnable() {
            thread = new Thread(this, "ClientRunnable");
            thread.start();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                long lastTime = System.currentTimeMillis();
                long lastTimeFps = System.currentTimeMillis();
                int simulationSteps = 0;

                while (running()) {
                    long currentTime = System.currentTimeMillis();
                    frameCount++;
                    if (currentTime - lastTimeFps > MS_IN_A_SECOND) {
                        fpsLabel.setText("FPS:" + Long.toString(frameCount));
                        lastTimeFps = currentTime;
                        frameCount = 0;
                    }

                    long diffTime = currentTime - lastTime;
                    long sleepTime = DESIRED_PERIOD - diffTime;
                    lastTime = currentTime;
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }

                    if (region != null && controller != null) {
                        if (simulationSteps++ > SIMULATION_STEPS_SEND_COMMAND) {
                            controller.doCommands(region);
                            simulationSteps = 0;
                            dateLabel.setText(region.getTimeString());
                            stateLabel.setText(worldPane.getStateString());
                        }

                        region.update();
                        worldPane.update();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                disconnect();
            }
        }

        /**
         * Running.
         * 
         * @return true, if successful
         */
        public synchronized boolean running() {
            return running;
        }

        /**
         * Stop.
         */
        public synchronized void stop() {
            running = false;
        }
    }

    /** The bounds of the window when it is created. */
    private static final Rectangle WINDOW_BOUNDS = new Rectangle(100, 100, 800, 600);

    /** The content pane. */
    private JPanel contentPane;

    /** The world pane. */
    private WorldPane worldPane;

    /** The jobs pane. */
    private JobsPane jobsPane;

    /** The labors pane. */
    private LaborsPane laborsPane;

    /** The stocks pane. */
    private StocksPane stocksPane;

    /** The player. */
    private Player player;

    /** The region. */
    private Region region;

    /** The controller. */
    private AbstractController controller;

    /** The client runnable. */
    private ClientRunnable clientRunnable;

    /** The status panel. */
    private JPanel statusPanel;

    /** The fps label. */
    private JLabel fpsLabel;

    /** The date label. */
    private JLabel dateLabel;

    /** The state label. */
    private JLabel stateLabel;

    /**
     * Instantiates a new main window.
     * 
     * @throws Exception the exception
     */
    public MainWindow() throws Exception {
        setTitle("Ben's Burrows");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        setupMainMenu();

        setVisible(true);
        setBounds(WINDOW_BOUNDS);
        setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        JOptionPane.showMessageDialog(this, "Connection lost");
        stop();
        setupMainMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSinglePlayerGame() {
        Logger.getInstance().log(this, "Loading game");
        try {
            setupForPlay();

            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();

            FileInputStream fileInputStream = new FileInputStream("myobject.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            region = (Region) objectInputStream.readObject();

            objectInputStream.close();

            player = region.getPlayers().get(0);
            controller = new SinglePlayerController();

            worldPane.setup(region, player, controller);
            jobsPane.setup(player.getJobManager());
            laborsPane.setup(player, controller);
            stocksPane.setup(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Save single player game.
     */
    public void saveSinglePlayerGame() {
        Logger.getInstance().log(this, "Saving game");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("myobject.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(region);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupHostMultiplayerGame() {
        String portString = Settings.getInstance().getSetting("default_port");

        if (portString == null) {
            return;
        }

        try {
            int port = Integer.parseInt(portString);

            LobbyPanel lobbyPanel = new LobbyPanel(LobbyType.SERVER, this);
            if (lobbyPanel.init(null, port)) {
                contentPane = lobbyPanel;
                setContentPane(lobbyPanel);
                lobbyPanel.start();
                revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Could not create server");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Port number is not integer");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupJoinMultiplayerGame() {
        String ip = JOptionPane.showInputDialog(this, "Host IP address?",
                Settings.getInstance().getSetting("default_ip"));

        if (ip == null) {
            return;
        }

        String portString = Settings.getInstance().getSetting("default_port");

        if (portString == null) {
            return;
        }

        try {
            int port = Integer.parseInt(portString);

            LobbyPanel lobbyPanel = new LobbyPanel(LobbyType.CLIENT, this);
            if (lobbyPanel.init(ip, port)) {
                contentPane = lobbyPanel;
                setContentPane(lobbyPanel);
                lobbyPanel.start();
                revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Could not connect to server");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Port number is not integer");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupMainMenu() {
        contentPane = new MainMenuPanel(this);
        setContentPane(contentPane);
        revalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupSinglePlayerGame() {
        contentPane = new SetupSinglePlayerGamePanel(this);
        setContentPane(contentPane);
        revalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startMultiplayerGame(final Connection connection, final List<String> playerNames,
            final int playerIndex, final MapIndex regionSize) {
        Logger.getInstance().log(this, "Starting multiplayer game");
        controller = new ClientController(connection, this);
        try {
            setupForPlay();

            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();

            region = new Region();
            region.setup(regionSize);

            for (String playerName : playerNames) {
                Logger.getInstance().log(this, "Adding player " + playerName);
                Player newPlayer = new Player(playerName);
                int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
                MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);
                region.addPlayer(newPlayer);
                if (playerName.equals(playerNames.get(playerIndex))) {
                    player = newPlayer;
                }
                newPlayer.setup(embarkPosition, numberOfStartingDwarfs, region.getMap());
            }

            worldPane.setup(region, player, controller);
            jobsPane.setup(player.getJobManager());
            laborsPane.setup(player, controller);
            stocksPane.setup(player);

            clientRunnable = new ClientRunnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startServer(final List<Connection> connections) {
        contentPane = new DedicatedServerPanel(connections, this);
        setContentPane(contentPane);
        revalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSinglePlayerGame(final String playerName, final MapIndex regionSize) {
        try {
            setupForPlay();

            ItemTypeManager.getInstance().load();
            WorkshopTypeManager.getInstance().load();
            LaborTypeManager.getInstance().load();
            RecipeManager.getInstance().load();

            region = new Region();
            region.setup(regionSize);

            int numberOfStartingDwarfs = Integer.parseInt(Settings.getInstance().getSetting("starting_dwarves"));
            MapIndex embarkPosition = new MapIndex(regionSize.x / 2, regionSize.y / 2, 0);

            player = new Player(playerName);
            player.setup(embarkPosition, numberOfStartingDwarfs, region.getMap());
            region.addPlayer(player);

            controller = new SinglePlayerController();

            worldPane.setup(region, player, controller);
            jobsPane.setup(player.getJobManager());
            laborsPane.setup(player, controller);
            stocksPane.setup(player);

            clientRunnable = new ClientRunnable();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not start the game");
            setupMainMenu();
        }
    }

    /**
     * Stop.
     */
    public void stop() {
        if (clientRunnable != null) {
            clientRunnable.stop();
            clientRunnable = null;
        }
    }

    /**
     * Setup for play.
     */
    private void setupForPlay() {
        contentPane = new ImagePanel();
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Create the status bar
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        fpsLabel = new JLabel("FPS");
        fpsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fpsLabel.setBorder(new EtchedBorder());
        statusPanel.add(fpsLabel);

        dateLabel = new JLabel("Date");
        dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dateLabel.setBorder(new EtchedBorder());
        statusPanel.add(dateLabel);

        stateLabel = new JLabel("State");
        stateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        stateLabel.setBorder(new EtchedBorder());
        statusPanel.add(stateLabel);

        // The world pane contains the main game canvas
        worldPane = new WorldPane();
        tabbedPane.addTab("World", null, worldPane, null);

        // The jobs pane has a list of all jobs
        jobsPane = new JobsPane();
        tabbedPane.addTab("Jobs", null, jobsPane, null);

        // The labors pane shows the skills and enabled labors for each dwarf
        laborsPane = new LaborsPane();
        tabbedPane.addTab("Labors", null, laborsPane, null);

        // The stocks pane shows all the stock counts
        stocksPane = new StocksPane();
        tabbedPane.addTab("Stocks", null, stocksPane, null);

        setContentPane(contentPane);
        revalidate();
    }
}
