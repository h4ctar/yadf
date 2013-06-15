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
import java.util.List;
import java.util.Map.Entry;

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.IMovementComponent;
import simulation.character.component.ISkillComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.ItemType;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.workshop.Workshop;
import simulation.workshop.WorkshopType;
import simulation.workshop.WorkshopTypeManager;

/**
 * The Class BuildWorkshopJob.
 */
public class BuildWorkshopJob extends AbstractJob {

    /**
     * The different states that this job can be in.
     */
    enum State {
        /** The waiting for resources. */
        WAITING_FOR_RESOURCES,
        /** The haul resources. */
        HAUL_RESOURCES,
        /** The waiting for dwarf. */
        WAITING_FOR_DWARF,
        /** The goto workshop. */
        GOTO_WORKSHOP,
        /** The build. */
        BUILD
    }

    /** The serial version UID. */
    private static final long serialVersionUID = 6619232211299027703L;

    /** How long it takes to build the workshop. */
    private static final long BUILD_TIME = Region.SIMULATION_STEPS_PER_DAY;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Building");

    /** The state. */
    private State state = State.WAITING_FOR_RESOURCES;

    /** The position. */
    private final MapIndex position;

    /** The workshop type. */
    private final WorkshopType workshopType;

    /** The dwarf. */
    private Dwarf dwarf;

    /** References to the haul jobs that are scheduled for materials. */
    private final List<HaulJob> haulJobs = new ArrayList<>();

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** The done. */
    private boolean done;

    /** The walk component. */
    private WalkMovementComponent walkComponent;

    /**
     * Instantiates a new builds the workshop job.
     * 
     * @param positionTmp the position
     * @param workshopTypeTmp the workshop type
     */
    public BuildWorkshopJob(final MapIndex positionTmp, final String workshopTypeTmp) {
        position = positionTmp;
        workshopType = WorkshopTypeManager.getInstance().getWorkshopType(workshopTypeTmp);
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAITING_FOR_RESOURCES:
            return "Waiting for resource";
        case HAUL_RESOURCES:
            return "Hauling resource";
        case GOTO_WORKSHOP:
            return "Builder walking to workshop";
        case BUILD:
            return "Building";
        case WAITING_FOR_DWARF:
            return "Waiting for dwarf";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Building " + workshopType.name;
    }

    @Override
    public void update(final Player player, final Region region) {
        switch (state) {
        case WAITING_FOR_RESOURCES:
            for (Entry<ItemType, Integer> entry : workshopType.resources.entrySet()) {
                ItemType itemType = entry.getKey();
                for (int i = 0; i < entry.getValue().intValue(); i++) {
                    HaulJob haulJob = new HaulJob(itemType, player.getStockManager(),
                            Workshop.getRandomPostition(position));
                    haulJobs.add(haulJob);
                }
            }
            state = State.HAUL_RESOURCES;
            break;

        case HAUL_RESOURCES:
            boolean resourcesDone = true;
            for (HaulJob haulJob : haulJobs) {
                haulJob.update(player, region);
                if (!haulJob.isDone()) {
                    resourcesDone = false;
                }
            }

            if (resourcesDone) {
                state = State.WAITING_FOR_DWARF;
            }
            break;

        case WAITING_FOR_DWARF:
            if (dwarf == null) {
                dwarf = player.getIdleDwarf(REQUIRED_LABOR);
            }

            if (dwarf != null) {
                walkComponent = new WalkMovementComponent(position, false);
                dwarf.setComponent(IMovementComponent.class, walkComponent);
                state = State.GOTO_WORKSHOP;
            }
            break;

        case GOTO_WORKSHOP:
            if (walkComponent.isNoPath()) {
                interrupt("No path to workshop");
                return;
            }

            if (walkComponent.isArrived()) {
                wasteTimeJob = new WasteTimeJob(dwarf, BUILD_TIME);
                state = State.BUILD;
            }
            break;

        case BUILD:
            wasteTimeJob.update(player, region);

            if (wasteTimeJob.isDone()) {
                for (HaulJob haulJob : haulJobs) {
                    haulJob.getItem().setRemove();
                }
                player.addWorkshop(new Workshop(workshopType, position));
                dwarf.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
                dwarf.releaseLock();
                done = true;
            }
            break;

        default:
            break;
        }
    }
}
