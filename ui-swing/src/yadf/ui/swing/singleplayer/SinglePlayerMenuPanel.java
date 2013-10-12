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
package yadf.ui.swing.singleplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import yadf.simulation.map.MapIndex;
import yadf.ui.swing.misc.ImagePanel;
import yadf.ui.swing.multiplayer.IMainWindow;

/**
 * The Class SetupSinglePlayerGamePanel.
 */
public class SinglePlayerMenuPanel extends ImagePanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 8023188031117400114L;

    /**
     * Action listener for the cancel game button.
     */
    private class CancelActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            mainWindow.setupMainMenu();
        }
    }

    /**
     * Action listener for the start game button.
     */
    private class StartGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            String playerName = playerNameTextField.getText();
            MapIndex regionSize = new MapIndex(64, 64, 32);
            mainWindow.startSinglePlayerGame(playerName, regionSize);
        }
    }

    /** The listener. */
    private final IMainWindow mainWindow;

    /** The player name text field. */
    private JTextField playerNameTextField;

    /** The lbl name. */
    private JLabel nameLabel;

    /** The region size combo box. */
    private JComboBox<String> regionSizeComboBox;

    /** The resources combo box. */
    private JComboBox<String> resourcesComboBox;

    /**
     * Create the panel.
     * 
     * @param mainWindowTmp the main window
     */
    public SinglePlayerMenuPanel(final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        setupLayout();
    }

    /**
     * Setup layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setLayout(new BorderLayout(5, 5));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        panelLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        panelLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
        panelLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(panelLayout);
        add(panel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Single Player Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        GridBagConstraints titleLabelConstraints = new GridBagConstraints();
        titleLabelConstraints.gridwidth = 4;
        titleLabelConstraints.insets = new Insets(0, 0, 5, 0);
        titleLabelConstraints.gridx = 0;
        titleLabelConstraints.gridy = 0;
        panel.add(titleLabel, titleLabelConstraints);

        nameLabel = new JLabel("Name");
        nameLabel.setForeground(Color.WHITE);
        GridBagConstraints nameLableConstraints = new GridBagConstraints();
        nameLableConstraints.insets = new Insets(5, 5, 5, 5);
        nameLableConstraints.anchor = GridBagConstraints.EAST;
        nameLableConstraints.gridx = 1;
        nameLableConstraints.gridy = 1;
        panel.add(nameLabel, nameLableConstraints);

        playerNameTextField = new JTextField();
        playerNameTextField.setText("Ben");
        GridBagConstraints playerNameTextFieldConstraints = new GridBagConstraints();
        playerNameTextFieldConstraints.insets = new Insets(5, 5, 5, 5);
        playerNameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        playerNameTextFieldConstraints.gridx = 2;
        playerNameTextFieldConstraints.gridy = 1;
        panel.add(playerNameTextField, playerNameTextFieldConstraints);
        playerNameTextField.setColumns(10);

        JLabel regionSizeLabel = new JLabel("Region Size");
        regionSizeLabel.setForeground(Color.WHITE);
        GridBagConstraints regionSizeLabelConstraints = new GridBagConstraints();
        regionSizeLabelConstraints.insets = new Insets(5, 5, 5, 5);
        regionSizeLabelConstraints.anchor = GridBagConstraints.EAST;
        regionSizeLabelConstraints.gridx = 1;
        regionSizeLabelConstraints.gridy = 2;
        panel.add(regionSizeLabel, regionSizeLabelConstraints);

        regionSizeComboBox = new JComboBox<>();
        regionSizeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Small", "Medium", "Large" }));
        regionSizeComboBox.setSelectedIndex(0);
        GridBagConstraints regionSizeComboBoxConstraints = new GridBagConstraints();
        regionSizeComboBoxConstraints.insets = new Insets(5, 5, 5, 5);
        regionSizeComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        regionSizeComboBoxConstraints.gridx = 2;
        regionSizeComboBoxConstraints.gridy = 2;
        panel.add(regionSizeComboBox, regionSizeComboBoxConstraints);

        JLabel resourcesLabel = new JLabel("Resources");
        resourcesLabel.setForeground(Color.WHITE);
        GridBagConstraints resourcesLabelConstraints = new GridBagConstraints();
        resourcesLabelConstraints.anchor = GridBagConstraints.EAST;
        resourcesLabelConstraints.insets = new Insets(5, 5, 5, 5);
        resourcesLabelConstraints.gridx = 1;
        resourcesLabelConstraints.gridy = 3;
        panel.add(resourcesLabel, resourcesLabelConstraints);

        resourcesComboBox = new JComboBox<>();
        resourcesComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Minimal", "Standard", "Plenty" }));
        GridBagConstraints resourcesComboBoxConstraints = new GridBagConstraints();
        resourcesComboBoxConstraints.insets = new Insets(5, 5, 5, 5);
        resourcesComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        resourcesComboBoxConstraints.gridx = 2;
        resourcesComboBoxConstraints.gridy = 3;
        panel.add(resourcesComboBox, resourcesComboBoxConstraints);

        JButton startGameButton = new JButton("Start Game");
        GridBagConstraints startGameButtonConstraints = new GridBagConstraints();
        startGameButtonConstraints.gridwidth = 2;
        startGameButtonConstraints.insets = new Insets(0, 0, 5, 5);
        startGameButtonConstraints.gridx = 1;
        startGameButtonConstraints.gridy = 4;
        panel.add(startGameButton, startGameButtonConstraints);
        startGameButton.addActionListener(new StartGameActionListener());

        JButton cancelButton = new JButton("Cancel");
        GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
        cancelButtonConstraints.gridwidth = 2;
        cancelButtonConstraints.insets = new Insets(0, 0, 5, 5);
        cancelButtonConstraints.gridx = 1;
        cancelButtonConstraints.gridy = 5;
        panel.add(cancelButton, cancelButtonConstraints);
        cancelButton.addActionListener(new CancelActionListener());
        // CHECKSTYLE:ON
    }
}
