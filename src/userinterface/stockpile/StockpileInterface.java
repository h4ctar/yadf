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
package userinterface.stockpile;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXTreeTable;

import simulation.Player;
import simulation.stock.Stockpile;
import controller.AbstractController;
import controller.command.DeleteStockpileCommand;

/**
 * The Class StockpileInterface.
 */
public class StockpileInterface extends JInternalFrame implements ActionListener {

    /** The stockpile. */
    private Stockpile stockpile;

    /** The controller. */
    private final AbstractController controller;

    /** The player. */
    private final Player player;

    /** The delete button. */
    private JButton deleteButton;

    /** The scroll pane. */
    private JScrollPane scrollPane;

    /** The tree table. */
    private JXTreeTable treeTable;

    /**
     * Create the frame.
     * 
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public StockpileInterface(final Player playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;
        setupLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        controller.addCommand(new DeleteStockpileCommand(player, stockpile.getId()));
        setVisible(false);
    }

    /**
     * Sets the stockpile.
     * 
     * @param stockpileTmp the new stockpile
     */
    public void setStockpile(final Stockpile stockpileTmp) {
        stockpile = stockpileTmp;
        StockpileTreeTableModel tableModel = new StockpileTreeTableModel(stockpile, controller, player);
        treeTable = new JXTreeTable(tableModel);
        scrollPane.setViewportView(treeTable);
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle("Stockpile interface");
        setResizable(true);
        setClosable(true);

        setBounds(100, 100, 450, 300);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0 };
        getContentPane().setLayout(gridBagLayout);

        scrollPane = new JScrollPane();
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.insets = new Insets(0, 0, 5, 0);
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 0;
        getContentPane().add(scrollPane, scrollPaneConstraints);

        treeTable = new JXTreeTable();
        scrollPane.setViewportView(treeTable);

        deleteButton = new JButton("Delete");
        GridBagConstraints deleteButtonConstraints = new GridBagConstraints();
        deleteButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        deleteButtonConstraints.gridx = 0;
        deleteButtonConstraints.gridy = 1;
        getContentPane().add(deleteButton, deleteButtonConstraints);
        deleteButton.addActionListener(this);
        // CHECKSTYLE:ON
    }
}
