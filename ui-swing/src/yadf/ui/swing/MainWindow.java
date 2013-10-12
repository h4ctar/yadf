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
package yadf.ui.swing;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import yadf.controller.Connection;
import yadf.settings.Settings;
import yadf.simulation.item.ItemTypeManager;
import yadf.simulation.labor.LaborTypeManager;
import yadf.simulation.map.MapIndex;
import yadf.simulation.recipe.RecipeManager;
import yadf.simulation.workshop.WorkshopTypeManager;
import yadf.ui.swing.game.GamePanel;
import yadf.ui.swing.howtoplay.HowToPlayPanel;
import yadf.ui.swing.mainmenu.MainMenuPanel;
import yadf.ui.swing.multiplayer.IMainWindow;
import yadf.ui.swing.multiplayer.LobbyPanel;
import yadf.ui.swing.multiplayer.LobbyType;
import yadf.ui.swing.server.DedicatedServerPanel;
import yadf.ui.swing.singleplayer.SinglePlayerMenuPanel;

/**
 * The Main Window.
 */
public class MainWindow extends JFrame implements IMainWindow {

    /** The serial version UID. */
    private static final long serialVersionUID = -3847562580085069500L;

    /** The bounds of the window when it is created. */
    private static final Rectangle WINDOW_BOUNDS = new Rectangle(100, 100, 1280, 800);

    /**
     * Instantiates a new main window and starts the main menu.
     * @throws Exception something went wrong
     */
    public MainWindow() throws Exception {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        setTitle("Ben's Burrows");
        setBounds(WINDOW_BOUNDS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupMainMenu();
        setVisible(true);
        ItemTypeManager.getInstance().load();
        WorkshopTypeManager.getInstance().load();
        LaborTypeManager.getInstance().load();
        RecipeManager.getInstance().load();
    }

    @Override
    public void quit() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

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

    @Override
    public void setupMainMenu() {
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        setContentPane(mainMenuPanel);
        revalidate();
    }

    @Override
    public void setupSinglePlayerGame() {
        SinglePlayerMenuPanel singlePlayerMenuPanel = new SinglePlayerMenuPanel(this);
        setContentPane(singlePlayerMenuPanel);
        revalidate();
    }

    @Override
    public void startSinglePlayerGame(final String playerName, final MapIndex regionSize) {
        GamePanel gamePanel = new GamePanel(this);
        setContentPane(gamePanel);
        gamePanel.startSinglePlayerGame(playerName, regionSize);
        revalidate();
    }

    @Override
    public void startMultiplayerGame(final Connection connection, final List<String> playerNames,
            final int playerIndex, final MapIndex regionSize) {
        GamePanel gamePanel = new GamePanel(this);
        setContentPane(gamePanel);
        gamePanel.startMultiplayerGame(connection, playerNames, playerIndex, regionSize);
        revalidate();
    }

    @Override
    public void startServer(final List<Connection> connections) {
        DedicatedServerPanel dedicatedServerPanel = new DedicatedServerPanel(connections, this);
        setContentPane(dedicatedServerPanel);
        revalidate();
    }

    @Override
    public void loadSinglePlayerGame() {
        GamePanel gamePanel = new GamePanel(this);
        setContentPane(gamePanel);
        gamePanel.loadSinglePlayerGame();
        revalidate();
    }

    @Override
    public void showHowToPlay() {
        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);
        setContentPane(howToPlayPanel);
        revalidate();
    }
}
