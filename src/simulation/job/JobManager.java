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
package simulation.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import simulation.IPlayer;
import simulation.job.designation.AbstractDesignation;
import simulation.job.designation.ChannelDesignation;
import simulation.job.designation.ChopTreeDesignation;
import simulation.job.designation.ConstructionDesignation;
import simulation.job.designation.DesignationType;
import simulation.job.designation.MineDesignation;
import simulation.map.BlockType;

/**
 * The job manager
 * 
 * This class is responsible for holding all the jobs, telling them to update and removing them when they are done, it
 * also contains the designations and launches jobs for them when required.
 */
public class JobManager implements IJobManager, Serializable, IJobListener {

    /** The serial version UID. */
    private static final long serialVersionUID = -3697576654461518146L;

    /** The jobs. */
    private final List<IJob> jobs = new ArrayList<>();

    /** The designations. */
    private final AbstractDesignation[] designations;

    /** Listeners to be notified when something changes. */
    private final List<IJobManagerListener> listeners = new ArrayList<>();

    /**
     * The constructor, sets up the designations.
     * @param player
     */
    public JobManager(final IPlayer player) {
        designations = new AbstractDesignation[DesignationType.values().length - 1]; // TODO: -1
        designations[DesignationType.MINE.ordinal()] = new MineDesignation(player);
        designations[DesignationType.CHANNEL.ordinal()] = new ChannelDesignation(player);
        designations[DesignationType.CHOP_TREE.ordinal()] = new ChopTreeDesignation(player);
        designations[DesignationType.BUILD_WALL.ordinal()] = new ConstructionDesignation(BlockType.WALL, player);
        designations[DesignationType.BUILD_RAMP.ordinal()] = new ConstructionDesignation(BlockType.RAMP, player);
        // TODO: carve stair
        // designations[DesignationType.CARVE_STAIR.ordinal()] = new ChannelDesignation(BlockType.STAIR);

        // This can't be done in the designations constructor because the player.getJobManager call would return null
        for (AbstractDesignation designation : designations) {
            jobs.add(designation);
        }
    }

    /**
     * Adds a new job.
     * @param job The job to add
     */
    @Override
    public void addJob(final IJob job) {
        Logger.getInstance().log(this, "Adding job: " + job.toString());
        job.addListener(this);
        jobs.add(job);
        for (IJobManagerListener listener : listeners) {
            listener.jobsAdded(job);
        }
    }

    @Override
    public void jobDone(final IJob job) {
        Logger.getInstance().log(this, "Removing job: " + job.toString());
        assert jobs.contains(job);
        jobs.remove(job);
        for (IJobManagerListener listener : listeners) {
            listener.jobRemoved(job);
        }
    }

    /**
     * Add a new listener to be notified when jobs change.
     * @param listener the listener to add.
     */
    public void addListener(final IJobManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets a reference to a particular designation.
     * @param type The designation type that the caller wants
     * @return A reference to the designation
     */
    public AbstractDesignation getDesignation(final DesignationType type) {
        return designations[type.ordinal()];
    }

    /**
     * Gets a reference to all the designations.
     * @return A reference to the designations array
     */
    public AbstractDesignation[] getDesignations() {
        return designations;
    }

    /**
     * Gets all the jobs.
     * @return the jobs
     */
    public List<IJob> getJobs() {
        return jobs;
    }
}
