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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulation.job.JobManager;

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
        add(jobsScrollPane, BorderLayout.CENTER);

        jobsTable = new JTable();
        jobsScrollPane.setViewportView(jobsTable);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.SOUTH);

        JButton cancelJobButton = new JButton("Cancel job");
        panel.add(cancelJobButton);

        JButton zoomToJobButton = new JButton("Zoom to job");
        panel.add(zoomToJobButton);
    }

    /**
     * Sets the up.
     * 
     * @param jobManager the new up
     */
    public void setup(final JobManager jobManager) {
        jobsTableModel = new JobsTableModel(jobManager);
        jobsTable.setModel(jobsTableModel);
    }
}
