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
package userinterface.game.job;

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
import javax.swing.border.EmptyBorder;

import simulation.job.IJobManager;

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

        JButton zoomToJobButton = new JButton("Zoom to job");
        zoomToJobButton.setMaximumSize(new Dimension(150, 23));
        zoomToJobButton.setMinimumSize(new Dimension(150, 23));
        zoomToJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomToJobButton.setPreferredSize(new Dimension(150, 23));
        zoomToJobButton.addActionListener(new ZoomToJobButtonActionListener());
        panel.add(zoomToJobButton);

        JButton cancelJobButton = new JButton("Cancel job");
        cancelJobButton.setMinimumSize(new Dimension(150, 23));
        cancelJobButton.setMaximumSize(new Dimension(150, 23));
        cancelJobButton.setPreferredSize(new Dimension(150, 23));
        cancelJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(cancelJobButton);
    }

    /**
     * Sets the job manager.
     * @param jobManager the new up
     * @param worldPane
     */
    public void setup(final IJobManager jobManager) {
        jobsTableModel = new JobsTableModel(jobManager);
        jobsTable.setModel(jobsTableModel);
    }

    /**
     * Action listener for the zoom to job button.
     */
    private class ZoomToJobButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }
}
