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

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.IMovementComponent;
import simulation.character.component.ISkillComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.WasteTimeJob;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.BlockType;
import simulation.map.MapIndex;
import simulation.map.RegionMap;

/**
 * The Class ChannelJob.
 */
public class ChannelJob extends AbstractDesignationJob {

    /**
     * The different states that this job can be in.
     */
    enum State {
        /** The start. */
        START,
        /** The goto. */
        GOTO,
        /** The channel. */
        CHANNEL
    }

    /** The serial version UID. */
    private static final long serialVersionUID = 117706611556221325L;

    /** Amount of time to spend channeling (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Mining");

    /** The state. */
    private State state = State.START;

    /** The position. */
    private final MapIndex position;

    /** The block type. */
    private final BlockType blockType;

    /** The designation. */
    private final ChannelDesignation designation;

    /** The move component. */
    private WalkMovementComponent moveComponent = null;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** The dwarf. */
    private Dwarf dwarf = null;

    /** The done. */
    private boolean done = false;

    /**
     * Instantiates a new channel job.
     * @param positionTmp the position
     * @param blockTypeTmp the block type
     * @param designationTmp the designation
     */
    public ChannelJob(final MapIndex positionTmp, final BlockType blockTypeTmp,
            final ChannelDesignation designationTmp) {
        position = positionTmp;
        blockType = blockTypeTmp;
        designation = designationTmp;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public String getStatus() {
        return "Channeling";
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        designation.removeFromDesignation(position);

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Channel job was interrupted");
        }

        if (dwarf != null) {
            dwarf.releaseLock();
        }

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Channel";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        RegionMap map = region.getMap();
        if (!map.getBlock(position.add(0, 0, -1)).isMineable) {
            interrupt("Block missing");
        }

        switch (state) {
        case START:
            dwarf = player.getDwarfManager().getIdleDwarf(REQUIRED_LABOR);
            if (dwarf != null) {
                moveComponent = new WalkMovementComponent(position, false);
                dwarf.setComponent(IMovementComponent.class, moveComponent);
                state = State.GOTO;
            }
            break;

        case GOTO:
            if (moveComponent.isNoPath()) {
                interrupt("No path to site");
                return;
            }

            if (moveComponent.isArrived()) {
                state = State.CHANNEL;
                wasteTimeJob = new WasteTimeJob(dwarf, DURATION);
            }
            break;

        case CHANNEL:
            wasteTimeJob.update(player, region);
            if (!wasteTimeJob.isDone()) {
                return;
            }
            if (blockType == null) {
                // create a rock item
                String itemTypeName = map.getBlock(position.add(0, 0, -1)).itemMined;

                if (itemTypeName != null) {
                    ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
                    Item blockItem = ItemTypeManager.getInstance().createItem(position, itemType, player);
                    player.getStockManager().addItem(blockItem);
                }
            }
            // channel the block
            map.channelBlock(position.add(0, 0, -1), blockType);
            // remove the tile from the designation
            designation.removeFromDesignation(position);
            dwarf.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            dwarf.releaseLock();
            done = true;
            break;

        default:
            break;
        }
    }
}
