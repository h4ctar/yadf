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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logger.Logger;
import simulation.IPlayer;
import simulation.IRegion;
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
public class JobManager implements IJobManager, IJobListener {

    /** The jobs. */
    private final List<IJob> jobs = new ArrayList<>();

    /** The designations. */
    private final Map<DesignationType, AbstractDesignation> designations = new HashMap<>();

    /** Listeners to be notified when something changes. */
    private final List<IJobManagerListener> listeners = new ArrayList<>();

    /**
     * Adds a new job.
     * @param job The job to add
     */
    @Override
    public void addJob(final IJob job) {
        Logger.getInstance().log(this, "Adding job: " + job.toString());
        job.addListener(this);
        jobs.add(job);
        int index = jobs.indexOf(job);
        for (IJobManagerListener listener : listeners) {
            listener.jobAdded(job, index);
        }
    }

    @Override
    public void jobDone(final IJob job) {
        assert jobs.contains(job);
        Logger.getInstance().log(this, "Removing job: " + job.toString());
        int index = jobs.indexOf(job);
        jobs.remove(job);
        for (IJobManagerListener listener : listeners) {
            listener.jobRemoved(job, index);
        }
    }

    @Override
    public void jobChanged(final IJob job) {
        // do nothing
    }

    /**
     * Add a new listener to be notified when jobs change.
     * @param listener the listener to add.
     */
    public void addListener(final IJobManagerListener listener) {
        listeners.add(listener);
    }

    @Override
    public AbstractDesignation getDesignation(final DesignationType type) {
        return designations.get(type);
    }

    @Override
    public Collection<AbstractDesignation> getDesignations() {
        return designations.values();
    }

    @Override
    public List<IJob> getJobs() {
        return jobs;
    }

    /**
     * Add all the designations.
     * @param region the region
     * @param player the player
     */
    public void addDesignations(final IRegion region, final IPlayer player) {
        designations.put(DesignationType.MINE, new MineDesignation(region, player));
        designations.put(DesignationType.CHANNEL, new ChannelDesignation(null, region, player));
        designations.put(DesignationType.CHOP_TREE, new ChopTreeDesignation(region, player));
        designations.put(DesignationType.BUILD_WALL, new ConstructionDesignation(BlockType.WALL, region, player));
        designations.put(DesignationType.BUILD_RAMP, new ConstructionDesignation(BlockType.RAMP, region, player));
        designations.put(DesignationType.CARVE_STAIR, new ChannelDesignation(BlockType.STAIR, region, player));
        for (AbstractDesignation designation : designations.values()) {
            addJob(designation);
        }
    }

    @Override
    public IJob getJob(final int jobId) {
        IJob foundJob = null;
        for (IJob job : jobs) {
            if (job.getId() == jobId) {
                foundJob = job;
                break;
            }
        }
        return foundJob;
    }

    @Override
    public void update() {
        // nothing to do
    }
}
