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
package userinterface.game.labor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import simulation.IPlayer;
import controller.AbstractController;

/**
 * The Class LaborsPane.
 */
public class LaborsPane extends JPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 1708672090502816986L;

    /** The labors table. */
    private final JTable laborsTable;

    /** The labor table model. */
    private LaborTableModel laborTableModel;

    /** The labours scroll pane. */
    private final JScrollPane laboursScrollPane;

    /**
     * Instantiates a new labors pane.
     */
    public LaborsPane() {
        super(new BorderLayout());
        setOpaque(false);

        laboursScrollPane = new JScrollPane();
        laboursScrollPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        add(laboursScrollPane, BorderLayout.CENTER);

        laborsTable = new JTable();
        laborsTable.setDefaultRenderer(LaborNode.class, new LaborRenderer());
        laborsTable.setDefaultEditor(LaborNode.class, new LaborEditor());
        laboursScrollPane.setViewportView(laborsTable);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.WEST);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton zoomButton = new JButton("Zoom to dwarf");
        zoomButton.setMinimumSize(new Dimension(150, 23));
        zoomButton.setMaximumSize(new Dimension(150, 23));
        zoomButton.setPreferredSize(new Dimension(150, 23));
        zoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(zoomButton);
    }

    /**
     * Setup.
     * @param player the player
     * @param controller the controller
     */
    public void setup(final IPlayer player, final AbstractController controller) {
        laborTableModel = new LaborTableModel(player, controller);
        laborsTable.setModel(laborTableModel);
        // TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        // Enumeration<TableColumn> columns = laborsTable.getColumnModel().getColumns();
        // while (columns.hasMoreElements()) {
        // columns.nextElement().setHeaderRenderer(headerRenderer);
        // }
    }
}
