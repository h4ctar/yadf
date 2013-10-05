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
package yadf.userinterface.game.job;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import yadf.controller.AbstractController;
import yadf.controller.command.CancelJobCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.designation.AbstractDesignation;
import yadf.simulation.map.MapIndex;
import yadf.userinterface.game.WorldPanel;

/**
 * The Class JobsPane.
 */
public class JobsPane extends JPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 3204746204349555430L;

    /** The jobs table. */
    private final JTable jobsTable;

    /** The jobs table model. */
    private JobsTableModel jobsTableModel;

    /** The jobs scroll pane. */
    private final JScrollPane jobsScrollPane;

    /** The world panel. */
    private WorldPanel worldPanel;

    /** The zoom to job button. */
    private JButton zoomToJobButton;

    /** The cancel job button. */
    private JButton cancelJobButton;

    /** The player. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Instantiates a new jobs pane.
     */
    public JobsPane() {
        super(new BorderLayout());
        setOpaque(false);

        jobsScrollPane = new JScrollPane();
        jobsScrollPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        add(jobsScrollPane, BorderLayout.CENTER);

        jobsTable = new JTable();
        jobsScrollPane.setViewportView(jobsTable);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.WEST);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        zoomToJobButton = new JButton("Zoom to job");
        zoomToJobButton.setMaximumSize(new Dimension(150, 23));
        zoomToJobButton.setMinimumSize(new Dimension(150, 23));
        zoomToJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomToJobButton.setPreferredSize(new Dimension(150, 23));
        zoomToJobButton.addActionListener(new ZoomToJobButtonActionListener());
        panel.add(zoomToJobButton);

        cancelJobButton = new JButton("Cancel job");
        cancelJobButton.setMinimumSize(new Dimension(150, 23));
        cancelJobButton.setMaximumSize(new Dimension(150, 23));
        cancelJobButton.setPreferredSize(new Dimension(150, 23));
        cancelJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelJobButton.addActionListener(new CancelJobButtonActionListener());
        panel.add(cancelJobButton);
    }

    /**
     * Sets the job manager.
     * @param playerTmp the player
     * @param controllerTmp the controller
     * @param worldPanelTmp the world panel
     */
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp, final WorldPanel worldPanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        worldPanel = worldPanelTmp;
        jobsTableModel = new JobsTableModel(player.getComponent(IJobManager.class));
        jobsTable.setModel(jobsTableModel);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobsTable.getSelectionModel().addListSelectionListener(new JobsTableListSelectionListener());
        zoomToJobButton.setEnabled(false);
        cancelJobButton.setEnabled(false);
    }

    /**
     * Action listener for the zoom to job button.
     */
    private class ZoomToJobButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            int row = jobsTable.getSelectedRow();
            if (row != -1) {
                IJob job = player.getComponent(IJobManager.class).getJobs().get(row);
                MapIndex position = job.getPosition();
                if (position != null) {
                    worldPanel.zoomToPosition(position);
                }
            }
        }
    }

    /**
     * Action listener for the cancel job button.
     */
    private class CancelJobButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            int row = jobsTable.getSelectedRow();
            if (row != -1) {
                IJob job = player.getComponent(IJobManager.class).getJobs().get(row);
                controller.addCommand(new CancelJobCommand(player, job.getId()));
            }
        }
    }

    /**
     * The jobs table list selection listener.
     * <p>
     * Enables and disables the buttons.
     */
    private class JobsTableListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            int row = jobsTable.getSelectedRow();
            if (row != -1) {
                IJob job = player.getComponent(IJobManager.class).getJobs().get(row);
                zoomToJobButton.setEnabled(job.getPosition() != null);
                cancelJobButton.setEnabled(!(job instanceof AbstractDesignation));
            } else {
                zoomToJobButton.setEnabled(false);
                cancelJobButton.setEnabled(false);
            }
        }
    }
}
