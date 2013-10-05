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
package yadf.userinterface.mainmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import yadf.userinterface.misc.ImagePanel;
import yadf.userinterface.multiplayer.IMainWindow;

/**
 * The Class MainMenuPanel.
 */
public class MainMenuPanel extends ImagePanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 2916881211506400113L;

    /** The listener. */
    private final IMainWindow mainWindow;

    /**
     * Instantiates a new main menu panel.
     * @param mainWindowTmp the main window
     */
    public MainMenuPanel(final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        setupLayout();
    }

    /**
     * Setup layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.CENTER);
        GridBagLayout panelConstraints = new GridBagLayout();
        panelConstraints.columnWidths = new int[] { 0, 0, 0, 0 };
        panelConstraints.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        panelConstraints.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        panelConstraints.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(panelConstraints);

        JLabel titleLabel = new JLabel("Ben's Burrows");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        GridBagConstraints titleLabelConstraints = new GridBagConstraints();
        titleLabelConstraints.gridwidth = 3;
        titleLabelConstraints.insets = new Insets(5, 5, 5, 0);
        titleLabelConstraints.gridx = 0;
        titleLabelConstraints.gridy = 0;
        panel.add(titleLabel, titleLabelConstraints);

        JButton newSinglePlayerGameButton = new JButton("New Single Player Game");
        GridBagConstraints newSinglePlayerGameButtonConstraints = new GridBagConstraints();
        newSinglePlayerGameButtonConstraints.fill = GridBagConstraints.VERTICAL;
        newSinglePlayerGameButtonConstraints.insets = new Insets(5, 5, 5, 5);
        newSinglePlayerGameButtonConstraints.gridx = 1;
        newSinglePlayerGameButtonConstraints.gridy = 1;
        panel.add(newSinglePlayerGameButton, newSinglePlayerGameButtonConstraints);
        newSinglePlayerGameButton.addActionListener(new SinglePlayerGameActionListener());

        JButton joinMultiplayerGameButton = new JButton("Join Multiplayer Game");
        GridBagConstraints joinMultiplayerGameButtonConstraints = new GridBagConstraints();
        joinMultiplayerGameButtonConstraints.fill = GridBagConstraints.VERTICAL;
        joinMultiplayerGameButtonConstraints.insets = new Insets(5, 5, 5, 5);
        joinMultiplayerGameButtonConstraints.gridx = 1;
        joinMultiplayerGameButtonConstraints.gridy = 2;
        panel.add(joinMultiplayerGameButton, joinMultiplayerGameButtonConstraints);
        joinMultiplayerGameButton.addActionListener(new JoinMultiplayerGameActionListener());

        JButton hostMultiplayerGameButton = new JButton("Host Multiplayer Game");
        GridBagConstraints hostMultiplayerGameButtonConstraints = new GridBagConstraints();
        hostMultiplayerGameButtonConstraints.fill = GridBagConstraints.VERTICAL;
        hostMultiplayerGameButtonConstraints.insets = new Insets(5, 5, 5, 5);
        hostMultiplayerGameButtonConstraints.gridx = 1;
        hostMultiplayerGameButtonConstraints.gridy = 3;
        panel.add(hostMultiplayerGameButton, hostMultiplayerGameButtonConstraints);
        hostMultiplayerGameButton.addActionListener(new HostMultiplayerGameActionListener());

        JButton loadSinglePlayerGameButton = new JButton("Load Single Player Game");
        GridBagConstraints loadSinglePlayerGameButtonConstraints = new GridBagConstraints();
        loadSinglePlayerGameButtonConstraints.fill = GridBagConstraints.VERTICAL;
        loadSinglePlayerGameButtonConstraints.insets = new Insets(5, 5, 5, 5);
        loadSinglePlayerGameButtonConstraints.gridx = 1;
        loadSinglePlayerGameButtonConstraints.gridy = 4;
        panel.add(loadSinglePlayerGameButton, loadSinglePlayerGameButtonConstraints);
        loadSinglePlayerGameButton.addActionListener(new LoadSinglePlayerGameActionListener());

        JButton howToPlayButton = new JButton("How to play");
        GridBagConstraints howToPlayButtonConstraints = new GridBagConstraints();
        howToPlayButtonConstraints.insets = new Insets(5, 5, 5, 5);
        howToPlayButtonConstraints.gridx = 1;
        howToPlayButtonConstraints.gridy = 5;
        howToPlayButton.addActionListener(new HowToPlayActionListener());
        panel.add(howToPlayButton, howToPlayButtonConstraints);

        JButton quitButton = new JButton("Quit");
        GridBagConstraints quitButtonConstraints = new GridBagConstraints();
        quitButtonConstraints.fill = GridBagConstraints.VERTICAL;
        quitButtonConstraints.insets = new Insets(5, 5, 5, 5);
        quitButtonConstraints.gridx = 1;
        quitButtonConstraints.gridy = 6;
        panel.add(quitButton, quitButtonConstraints);
        quitButton.addActionListener(new QuitActionListener());
        // CHECKSTYLE:ON
    }

    /**
     * Action listener for the host multiplayer game button.
     */
    private class HostMultiplayerGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.setupHostMultiplayerGame();
        }
    }

    /**
     * Action listener for the join multiplayer game button.
     */
    private class JoinMultiplayerGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.setupJoinMultiplayerGame();
        }
    }

    /**
     * Action listener for the load single player game button.
     */
    private class LoadSinglePlayerGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.loadSinglePlayerGame();
        }
    }

    /**
     * Action listener for the quit button.
     */
    private class QuitActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.quit();
        }
    }

    /**
     * Action listener for the how to play button.
     */
    public class HowToPlayActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.showHowToPlay();
        }
    }

    /**
     * Action listener for the single player game button.
     */
    private class SinglePlayerGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.setupSinglePlayerGame();
        }
    }
}
