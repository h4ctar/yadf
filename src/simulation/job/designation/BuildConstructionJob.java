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

import java.util.List;

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.WalkMoveComponent;
import simulation.item.Item;
import simulation.item.ItemTypeManager;
import simulation.job.HaulJob;
import simulation.job.WasteTimeJob;
import simulation.labor.LaborTypeManager;
import simulation.map.BlockType;
import simulation.map.MapIndex;
import simulation.map.WalkableNode;

/**
 * The Class BuildConstructionJob.
 */
public class BuildConstructionJob extends AbstractDesignationJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -6711242158871047234L;

    /**
     * The different states that this job can be in.
     */
    enum State {
        /** The waiting for resources state. */
        WAITING_FOR_RESOURCES,
        /** The haul resources state. */
        HAUL_RESOURCES,
        /** The waiting for dwarf state. */
        WAITING_FOR_DWARF,
        /** The dwarf walking to workshop state. */
        GOTO_SITE,
        /** The craft state. */
        BUILDING
    }

    /** The current state of the job. */
    private State state = State.WAITING_FOR_RESOURCES;

    /** The position. */
    private final MapIndex position;

    /** The construction type. */
    private final BlockType constructionType;

    /** The designation. */
    private final ConstructionDesignation designation;

    /** References to the haul jobs that are scheduled for materials. */
    private HaulJob haulJob;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** Amount of time to spend building (simulation steps). */
    private static final long BUILD_DURATION = Region.SIMULATION_STEPS_PER_HOUR * 2;

    /** The dwarf. */
    private Dwarf dwarf = null;

    /** The done. */
    private boolean done = false;

    /** The walk component. */
    private WalkMoveComponent walkComponent;

    /**
     * Instantiates a new builds the construction job.
     * 
     * @param positionTmp the position
     * @param constructionTypeTmp the construction type
     * @param designationTmp the designation
     */
    public BuildConstructionJob(final MapIndex positionTmp, final BlockType constructionTypeTmp,
            final ConstructionDesignation designationTmp) {
        position = positionTmp;
        constructionType = constructionTypeTmp;
        designation = designationTmp;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAITING_FOR_RESOURCES:
            return "Waiting on resources";
        case HAUL_RESOURCES:
            return "Hauling resources";
        case WAITING_FOR_DWARF:
            return "Waiting for builder";
        case GOTO_SITE:
            return "Walking to site";
        case BUILDING:
            return "Building";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message);

        if (dwarf != null) {
            dwarf.beIdleMovement();
            dwarf.releaseLock();
        }

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Construction job was canceled");
        }

        designation.removeFromDesignation(position);

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Build " + constructionType;
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAITING_FOR_RESOURCES:
            haulJob = new HaulJob(ItemTypeManager.getInstance().getItemType("Rock"), player.getStockManager(),
                    position);
            state = State.HAUL_RESOURCES;
            break;

        case HAUL_RESOURCES:
            haulJob.update(player, region);
            if (haulJob.isDone()) {
                state = State.WAITING_FOR_DWARF;
            }
            break;

        case WAITING_FOR_DWARF:
            if (dwarf == null) {
                dwarf = player.getIdleDwarf(LaborTypeManager.getInstance().getLaborType("Building"));
            }

            if (dwarf != null) {
                walkComponent = dwarf.walkToPosition(position, false);
                state = State.GOTO_SITE;
            }
            break;

        case GOTO_SITE:
            if (walkComponent.isNoPath()) {
                interrupt("No path to workshop");
                return;
            }

            if (walkComponent.isArrived()) {
                wasteTimeJob = new WasteTimeJob(dwarf, BUILD_DURATION);
                state = State.BUILDING;
            }
            break;

        case BUILDING:
            wasteTimeJob.update(player, region);

            if (wasteTimeJob.isDone()) {
                haulJob.getItem().setRemove();

                // Move items
                for (Item item : player.getStockManager().getItems()) {
                    if (item.getPosition().equals(position)) {
                        List<WalkableNode> adjacencies = region.getMap().getAdjacencies(position);

                        if (!adjacencies.isEmpty()) {
                            item.setPosition(adjacencies.get(0));
                        }
                    }
                }

                region.getMap().setBlock(position, constructionType);
                designation.removeFromDesignation(position);

                dwarf.getSkill().increaseSkillLevel(LaborTypeManager.getInstance().getLaborType("Building"));
                dwarf.releaseLock();

                done = true;
            }
            break;

        default:
            break;
        }
    }
}
