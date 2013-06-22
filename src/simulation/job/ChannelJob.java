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

import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.ISkillComponent;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.designation.ChannelDesignation;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForDwarfState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;

/**
 * The Class ChannelJob.
 */
public class ChannelJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 117706611556221325L;

    /** Amount of time to spend channeling (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Mining");

    /** The position. */
    private final MapIndex position;

    /** The designation. */
    private final ChannelDesignation designation;

    /** The miner dwarf. */
    private Dwarf miner;

    /**
     * Instantiates a new channel job.
     * @param positionTmp the position
     * @param designationTmp the designation
     */
    public ChannelJob(final MapIndex positionTmp, final ChannelDesignation designationTmp) {
        super(designationTmp.getPlayer());
        position = positionTmp;
        designation = designationTmp;
        setJobState(new LookingForMinerState());
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Channel";
    }

    /**
     * The looking for miner job state.
     */
    private class LookingForMinerState extends LookingForDwarfState {

        /**
         * Constructor.
         */
        public LookingForMinerState() {
            super(REQUIRED_LABOR, ChannelJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            miner = getDwarf();
        }

        @Override
        public IJobState getNextState() {
            return new WalkToChannelingSiteState();
        }
    }

    /**
     * The walk to channel site job state.
     */
    private class WalkToChannelingSiteState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToChannelingSiteState() {
            super(position, miner, ChannelJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new ChannelState();
        }
    }

    /**
     * The channel job state.
     */
    private class ChannelState extends WasteTimeState {

        /**
         * Constructor.
         */
        public ChannelState() {
            super(DURATION, miner, ChannelJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            // create a rock item
            String itemTypeName = designation.getRegion().getMap().getBlock(position.add(0, 0, -1)).itemMined;

            if (itemTypeName != null) {
                ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
                Item blockItem = ItemTypeManager.getInstance().createItem(position, itemType, getPlayer());
                getPlayer().getStockManager().addItem(blockItem);
            }
            // channel the block
            designation.getRegion().getMap().channelBlock(position.add(0, 0, -1), null);
            miner.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            miner.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
