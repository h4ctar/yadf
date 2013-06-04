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
package userinterface.menus.multiplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logger.Logger;
import misc.Utility;
import simulation.map.MapIndex;
import userinterface.menus.lobby.client.LobbyClient;
import userinterface.menus.lobby.server.LobbyServer;
import userinterface.misc.ImagePanel;
import controller.Connection;

/**
 * The Class LobbyPanel.
 * 
 * @author Ben
 */
public class LobbyPanel extends ImagePanel implements ILobbyPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = -5926430872507395857L;

    /**
     * Cancel the session, could be called if client or server.
     */
    private class CancelActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            lobby.stop();
            lobby.close();
            cancel();
        }
    }

    /**
     * Sends a chat test.
     */
    private class ChatActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            sendChat();
        }
    }

    /**
     * Start game action, this action will only ever be called by a server lobby.
     */
    private class StartGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            ((LobbyServer) lobby).startGame();
        }
    }

    /** The type of the lobby. */
    private final LobbyType lobbyType;

    /** The lobby. */
    private ILobby lobby;

    /** The listener. */
    private final IMainWindow mainWindow;

    /** The player table model. */
    private PlayerTableModel playerTableModel;

    /** The start game button. */
    private JButton startGameButton;

    /** The cancel button. */
    private JButton cancelButton;

    /** The resources combo box. */
    private JComboBox<String> resourcesComboBox;

    /** The region size combo box. */
    private JComboBox<String> regionSizeComboBox;

    /** The chat text area. */
    private JTextArea chatTextArea;

    /** The chat text field. */
    private JTextField chatTextField;

    /** The players table. */
    private JTable playersTable;

    /**
     * Instantiates a new lobby panel.
     * @param lobbyTypeTmp the lobby type
     * @param listenerTmp the listener
     */
    public LobbyPanel(final LobbyType lobbyTypeTmp, final IMainWindow listenerTmp) {
        lobbyType = lobbyTypeTmp;
        mainWindow = listenerTmp;

        if (lobbyType == LobbyType.SERVER) {
            lobby = new LobbyServer(this);
        } else {
            lobby = new LobbyClient(this);
        }

        setupLayout();
    }

    @Override
    public void cancel() {
        mainWindow.setupMainMenu();
    }

    @Override
    public void disconnect() {
        mainWindow.setupMainMenu();
    }

    /**
     * Inits the lobby.
     * @param ip the ip
     * @param port the port
     * @return true, if successful
     */
    public boolean init(final String ip, final int port) {
        return lobby.init(ip, port);
    }

    @Override
    public void receiveChat(final String playerName, final String text) {
        chatTextArea.append(playerName + " : " + text + "\n");
    }

    @Override
    public void setPlayerIndex(final int index) {
        playerTableModel.setPlayerIndex(index);
    }

    @Override
    public void setPlayerNames(final List<String> playerNames) {
        playerTableModel.setPlayerNames(playerNames);
    }

    /**
     * Start.
     */
    public void start() {
        lobby.start();
    }

    @Override
    public void startGame() {
        Logger.getInstance().log(this, "Starting game");
        LobbyClient lobbyClient = (LobbyClient) lobby;
        Connection connection = lobbyClient.getConnection();
        List<String> playerNames = playerTableModel.getPlayerNames();
        int playerIndex = playerTableModel.getPlayerIndex();
        MapIndex regionSize = Utility.getRegionSize(regionSizeComboBox);

        mainWindow.startMultiplayerGame(connection, playerNames, playerIndex, regionSize);
    }

    @Override
    public void startServer() {
        LobbyServer lobbyServer = (LobbyServer) lobby;
        List<Connection> connections = lobbyServer.getConnections();
        mainWindow.startServer(connections);
    }

    /**
     * Stop.
     */
    public void stop() {
        lobby.stop();
    }

    /**
     * Send a chat text.
     */
    private void sendChat() {
        String playerName;
        String text = chatTextField.getText();
        chatTextField.setText(null);
        if (lobbyType == LobbyType.SERVER) {
            playerName = "Host";
            receiveChat(playerName, text);
        } else {
            playerName = playerTableModel.getPlayerName();
        }
        lobby.sendChat(playerName, text);
    }

    /**
     * Setup layout.
     */
    // CHECKSTYLE:OFF
    private void setupLayout() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(5, 5));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        add(panel_1, BorderLayout.NORTH);

        JLabel multiplayerGameLabel = new JLabel("Multiplayer Game");
        panel_1.add(multiplayerGameLabel);
        multiplayerGameLabel.setForeground(Color.WHITE);
        multiplayerGameLabel.setFont(new Font("Minecraftia", Font.PLAIN, 24));
        multiplayerGameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.EAST);
        GridBagLayout panelConstraints = new GridBagLayout();
        panelConstraints.columnWidths = new int[] { 0, 200 };
        panelConstraints.rowHeights = new int[] { 0, 0, 0 };
        panelConstraints.columnWeights = new double[] { 0.0, 1.0 };
        panelConstraints.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(panelConstraints);

        JLabel regionSizeLabel = new JLabel("Region Size");
        regionSizeLabel.setForeground(Color.WHITE);
        GridBagConstraints regionSizeLabelConstraints = new GridBagConstraints();
        regionSizeLabelConstraints.anchor = GridBagConstraints.EAST;
        regionSizeLabelConstraints.insets = new Insets(5, 5, 5, 5);
        regionSizeLabelConstraints.gridx = 0;
        regionSizeLabelConstraints.gridy = 0;
        panel.add(regionSizeLabel, regionSizeLabelConstraints);

        regionSizeComboBox = new JComboBox<>();
        regionSizeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Small", "Medium", "Large" }));
        regionSizeComboBox.setSelectedIndex(1);
        GridBagConstraints regionSizeComboBoxConstraints = new GridBagConstraints();
        regionSizeComboBoxConstraints.insets = new Insets(5, 5, 5, 5);
        regionSizeComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        regionSizeComboBoxConstraints.gridx = 1;
        regionSizeComboBoxConstraints.gridy = 0;
        panel.add(regionSizeComboBox, regionSizeComboBoxConstraints);

        JLabel resourcesLabel = new JLabel("Resources");
        resourcesLabel.setForeground(Color.WHITE);
        GridBagConstraints resourcesLabelConstraints = new GridBagConstraints();
        resourcesLabelConstraints.anchor = GridBagConstraints.EAST;
        resourcesLabelConstraints.insets = new Insets(5, 5, 5, 5);
        resourcesLabelConstraints.gridx = 0;
        resourcesLabelConstraints.gridy = 1;
        panel.add(resourcesLabel, resourcesLabelConstraints);

        resourcesComboBox = new JComboBox<>();
        resourcesComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Minimal", "Standard", "Plenty" }));
        resourcesComboBox.setSelectedIndex(0);
        GridBagConstraints resourcesComboBoxConstraints = new GridBagConstraints();
        resourcesComboBoxConstraints.insets = new Insets(5, 5, 5, 5);
        resourcesComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        resourcesComboBoxConstraints.gridx = 1;
        resourcesComboBoxConstraints.gridy = 1;
        panel.add(resourcesComboBox, resourcesComboBoxConstraints);

        JPanel panel2 = new JPanel();
        panel2.setOpaque(false);
        add(panel2, BorderLayout.SOUTH);
        GridLayout panel2Constraints = new GridLayout();
        panel2Constraints.setVgap(5);
        panel2Constraints.setHgap(5);
        panel2.setLayout(panel2Constraints);

        if (lobbyType == LobbyType.SERVER) {
            startGameButton = new JButton("Start Game");
            panel2.add(startGameButton);
            startGameButton.addActionListener(new StartGameActionListener());
        }

        cancelButton = new JButton("Cancel");
        panel2.add(cancelButton);
        cancelButton.addActionListener(new CancelActionListener());

        JPanel panel3 = new JPanel();
        panel3.setOpaque(false);
        add(panel3, BorderLayout.CENTER);
        GridBagLayout panel3Constraints = new GridBagLayout();
        panel3Constraints.columnWidths = new int[] { 575, 0 };
        panel3Constraints.rowHeights = new int[] { 0, 248, 0, 0, 0, 0 };
        panel3Constraints.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        panel3Constraints.rowWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        panel3.setLayout(panel3Constraints);

        JLabel playersLabel = new JLabel("Players");
        playersLabel.setForeground(Color.WHITE);
        GridBagConstraints playersLabelConstraints = new GridBagConstraints();
        playersLabelConstraints.insets = new Insets(5, 5, 5, 0);
        playersLabelConstraints.anchor = GridBagConstraints.WEST;
        playersLabelConstraints.gridx = 0;
        playersLabelConstraints.gridy = 0;
        panel3.add(playersLabel, playersLabelConstraints);

        JScrollPane playersScrollPane = new JScrollPane();
        GridBagConstraints playersScrollPaneConstraints = new GridBagConstraints();
        playersScrollPaneConstraints.insets = new Insets(0, 5, 5, 0);
        playersScrollPaneConstraints.fill = GridBagConstraints.BOTH;
        playersScrollPaneConstraints.gridx = 0;
        playersScrollPaneConstraints.gridy = 1;
        panel3.add(playersScrollPane, playersScrollPaneConstraints);

        playerTableModel = new PlayerTableModel();
        playersTable = new JTable(playerTableModel);
        playersScrollPane.setViewportView(playersTable);

        JLabel chatLabel = new JLabel("Chat");
        chatLabel.setForeground(Color.WHITE);
        GridBagConstraints chatLabelConstraints = new GridBagConstraints();
        chatLabelConstraints.anchor = GridBagConstraints.WEST;
        chatLabelConstraints.insets = new Insets(5, 5, 5, 0);
        chatLabelConstraints.gridx = 0;
        chatLabelConstraints.gridy = 2;
        panel3.add(chatLabel, chatLabelConstraints);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.insets = new Insets(0, 5, 5, 0);
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 3;
        panel3.add(scrollPane, scrollPaneConstraints);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setTabSize(0);
        chatTextArea.setLineWrap(true);
        scrollPane.setViewportView(chatTextArea);

        chatTextField = new JTextField();
        GridBagConstraints chatTextFielConstraints = new GridBagConstraints();
        chatTextFielConstraints.insets = new Insets(0, 5, 0, 0);
        chatTextFielConstraints.fill = GridBagConstraints.HORIZONTAL;
        chatTextFielConstraints.gridx = 0;
        chatTextFielConstraints.gridy = 4;
        panel3.add(chatTextField, chatTextFielConstraints);
        chatTextField.setColumns(10);
        chatTextField.addActionListener(new ChatActionListener());
        // CHECKSTYLE:ON
    }
}
