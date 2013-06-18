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

import javax.swing.table.AbstractTableModel;

import simulation.job.IJob;
import simulation.job.IJobManagerListener;
import simulation.job.JobManager;

/**
 * The Class JobsTableModel.
 */
public class JobsTableModel extends AbstractTableModel implements IJobManagerListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 4018365907743267846L;

    /** The job manager. */
    private final JobManager jobManager;

    /**
     * Instantiates a new jobs table model.
     * 
     * @param jobManagerTmp the job manager
     */
    public JobsTableModel(final JobManager jobManagerTmp) {
        jobManager = jobManagerTmp;
        jobManager.addListener(this);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "Job";
        }

        return "Status";
    }

    @Override
    public int getRowCount() {
        return jobManager.getJobs().size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        try {
            IJob job = jobManager.getJobs().get(rowIndex);
            if (columnIndex == 0) {
                return job.toString();
            }

            return job.getStatus();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void jobsAdded(final int firstIndex, final int lastIndex) {
        this.fireTableRowsInserted(firstIndex, lastIndex);
    }

    @Override
    public void jobRemoved(IJob job) {
        // this.fireTableRowsDeleted(index, index);
        // TODO: fix this
    }
}
