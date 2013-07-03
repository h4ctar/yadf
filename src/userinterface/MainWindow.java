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

import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import settings.Settings;
import simulation.map.MapIndex;
import userinterface.game.GamePanel;
import userinterface.howtoplay.HowToPlayPanel;
import userinterface.mainmenu.MainMenuPanel;
import userinterface.multiplayer.IMainWindow;
import userinterface.multiplayer.LobbyPanel;
import userinterface.multiplayer.LobbyType;
import userinterface.server.DedicatedServerPanel;
import userinterface.singleplayer.SinglePlayerMenuPanel;
import controller.Connection;

/**
 * The Main Window.
 */
public class MainWindow extends JFrame implements IMainWindow {

    /** The serial version UID. */
    private static final long serialVersionUID = -3847562580085069500L;

    /** The bounds of the window when it is created. */
    private static final Rectangle WINDOW_BOUNDS = new Rectangle(100, 100, 800, 600);

    /** The default font. */
    private static final Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 14);

    /** The default font for the internal frame titles, needs to be this because the minecraftia font is too large. */
    private static final Font INTERNAL_FRAME_FONT = new Font("Tahoma", Font.PLAIN, 12);

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
        UIManager.getLookAndFeelDefaults().put("defaultFont", DEFAULT_FONT);
        UIManager.put("InternalFrame.titleFont", INTERNAL_FRAME_FONT);
        setTitle("Ben's Burrows");
        setBounds(WINDOW_BOUNDS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
        setupMainMenu();
        setVisible(true);
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
