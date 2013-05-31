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
package simulation.job.designation;

import java.util.ArrayList;
import java.util.List;

import simulation.Player;
import simulation.Region;
import simulation.job.IJob;
import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * Abstract class for a designation Designations are groups or areas of jobs to do that can be added to or subtracted
 * from and spawn jobs.
 * 
 * @author Ben Smith (bensmith87@gmail.com)
 */
public abstract class Designation implements IJob {

    /** The jobs. */
    protected List<IDesignationJob> jobs = new ArrayList<>();

    /** The map indicies. */
    protected List<MapIndex> mapIndicies = new ArrayList<>();

    /** The new map indicies. */
    protected List<MapIndex> newMapIndicies = new ArrayList<>();

    /**
     * Add an area to the designation.
     * 
     * @param area An area to add to the designation
     */
    public void addToDesignation(final MapArea area) {
        for (int x = area.pos.x; x < area.pos.x + area.width; x++) {
            for (int y = area.pos.y; y < area.pos.y + area.height; y++) {
                newMapIndicies.add(new MapIndex(x, y, area.pos.z));
            }
        }
    }

    /**
     * Gets all the map indicies that are in the designation, used by the GUI to display the designation.
     * 
     * @return A vector of map indicies
     */
    public List<MapIndex> getMapIndicies() {
        return mapIndicies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        if (mapIndicies.isEmpty()) {
            return "Empty";
        }

        return mapIndicies.size() + " locations";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interrupt(final String message) {
        /* This should never be interrupted */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Removes an area from the designation, and removes the job.
     * 
     * @param area The area to be removed
     */
    public void removeFromDesignation(final MapArea area) {
        for (int x = area.pos.x; x < area.pos.x + area.width; x++) {
            for (int y = area.pos.y; y < area.pos.y + area.height; y++) {
                // remove the index
                for (MapIndex mapIndex : mapIndicies) {
                    MapIndex mapIndex2 = new MapIndex(x, y, area.pos.z);
                    if (mapIndex2.equals(mapIndex)) {
                        mapIndicies.remove(mapIndex);
                        removeJob(mapIndex);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Removes a location from the designation.
     * 
     * @param mapIndex2 The location to be removed
     */
    public void removeFromDesignation(final MapIndex mapIndex2) {
        for (MapIndex mapIndex : mapIndicies) {
            if (mapIndex2.equals(mapIndex)) {
                mapIndicies.remove(mapIndex);
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Player player, final Region region) {
        addNewJobs(region);

        for (IJob job : jobs) {
            job.update(player, region);
        }

        removeDoneJobs();
    }

    /**
     * Adds the new jobs.
     * 
     * @param region the region
     */
    private void addNewJobs(final Region region) {
        for (MapIndex mapIndex : newMapIndicies) {
            if (!valid(mapIndex, region)) {
                continue;
            }

            boolean alreadyAdded = false;
            for (MapIndex existingMapIndex : mapIndicies) {
                if (existingMapIndex.equals(mapIndex)) {
                    alreadyAdded = true;
                    break;
                }
            }

            if (!alreadyAdded) {
                jobs.add(createJob(mapIndex, region));
                mapIndicies.add(mapIndex);
            }
        }

        newMapIndicies.clear();
    }

    /**
     * Removes the done jobs.
     */
    private void removeDoneJobs() {
        for (int i = 0; i < jobs.size(); i++) {
            IJob job = jobs.get(i);
            if (job.isDone()) {
                jobs.remove(i);
                i--;
            }
        }
    }

    /**
     * Method to remove a job created by this designation, this will be called when an area is removed from the
     * designation.
     * 
     * @param index The location of the job to be removed
     */
    private void removeJob(final MapIndex index) {
        for (IDesignationJob job : jobs) {
            if (job.getPosition().equals(index)) {
                job.interrupt("Designation removed");
                return;
            }
        }
    }

    /**
     * Template method to be filled out by concrete sub classes that adds a job to a job manager for this particular
     * designation.
     * 
     * @param mapIndex The location of the job
     * @param region the region
     * @return the i designation job
     */
    protected abstract IDesignationJob createJob(MapIndex mapIndex, Region region);

    /**
     * Template method that should return if a particular location(map index) is valid for the particular designation
     * type.
     * 
     * @param mapIndex The location to check
     * @param region the region
     * @return True if location is valid otherwise false
     */
    protected abstract boolean valid(MapIndex mapIndex, Region region);
}
