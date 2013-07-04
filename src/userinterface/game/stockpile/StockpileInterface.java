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
package userinterface.game.stockpile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTreeTable;

import simulation.IPlayer;
import simulation.item.Stockpile;
import userinterface.game.WorldCanvas;
import controller.AbstractController;
import controller.command.DeleteStockpileCommand;

/**
 * The Class StockpileInterface.
 */
public class StockpileInterface extends JPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = -7914475811407054901L;

    /** The stockpile. */
    private Stockpile stockpile;

    /** The controller. */
    private AbstractController controller;

    /** The player. */
    private IPlayer player;

    /** The delete button. */
    private JButton deleteButton;

    /** The scroll pane. */
    private JScrollPane scrollPane;

    /** The tree table. */
    private JXTreeTable treeTable;

    /** The panel the holds the buttons. */
    private JPanel buttonPanel;

    /** A button that zooms to the stockpile. */
    private JButton zoomButton;

    /** The canvas (required to zoom to the stockpile). */
    private WorldCanvas worldCanvas;

    /**
     * Create the frame.
     */
    public StockpileInterface() {
        setOpaque(false);
        setupLayout();
    }

    /**
     * Sets the stockpile.
     * @param stockpileTmp the new stockpile
     * @param worldCanvasTmp the canvas (required to zoom to the stockpile)
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public void setStockpile(final Stockpile stockpileTmp, final WorldCanvas worldCanvasTmp, final IPlayer playerTmp,
            final AbstractController controllerTmp) {
        stockpile = stockpileTmp;
        worldCanvas = worldCanvasTmp;
        player = playerTmp;
        controller = controllerTmp;
        StockpileTreeTableModel tableModel = new StockpileTreeTableModel(stockpile, controller, player);
        treeTable = new JXTreeTable(tableModel);
        scrollPane.setViewportView(treeTable);
    }

    /**
     * Action listener for the delete button.
     */
    private class ZoomButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            worldCanvas.zoomToArea(stockpile.getArea());
        }
    }

    /**
     * Action listener for the delete button.
     */
    private class DeleteButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            controller.addCommand(new DeleteStockpileCommand(player, stockpile.getId()));
            setVisible(false);
        }
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.insets = new Insets(0, 0, 5, 0);
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 0;

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
        gbc_buttonPanel.fill = GridBagConstraints.BOTH;
        gbc_buttonPanel.gridx = 0;
        gbc_buttonPanel.gridy = 1;
        add(buttonPanel, BorderLayout.WEST);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        zoomButton = new JButton("Zoom");
        zoomButton.setMinimumSize(new Dimension(150, 23));
        zoomButton.setMaximumSize(new Dimension(150, 23));
        zoomButton.setPreferredSize(new Dimension(150, 23));
        zoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(zoomButton);
        zoomButton.addActionListener(new ZoomButtonActionListener());

        deleteButton = new JButton("Delete");
        deleteButton.setMinimumSize(new Dimension(150, 23));
        deleteButton.setMaximumSize(new Dimension(150, 23));
        deleteButton.setPreferredSize(new Dimension(150, 23));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(deleteButton);

        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        add(scrollPane);

        treeTable = new JXTreeTable();
        scrollPane.setViewportView(treeTable);
        deleteButton.addActionListener(new DeleteButtonActionListener());
    }
}
