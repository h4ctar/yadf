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

import simulation.IPlayer;
import simulation.Region;
import simulation.character.IGameCharacter;
import simulation.character.component.ISkillComponent;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForDwarfState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.BlockType;
import simulation.map.MapIndex;
import simulation.map.RegionMap;

/**
 * The Class ChannelJob.
 */
public class ChannelJob extends AbstractJob {

    /** Amount of time to spend channeling (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Mining");

    /** The position. */
    private final MapIndex position;

    /** The miner dwarf. */
    private IGameCharacter miner;

    /** The type of block to replace the channeled block with, null to totally remove it. */
    private final BlockType blockType;

    /** The map that will be channeled. */
    final RegionMap map;

    /**
     * Instantiates a new channel job.
     * @param positionTmp the position
     * @param blockTypeTmp the type of block to replace the channeled block with, null to totally remove it
     * @param mapTmp the map that the construction will be built in
     * @param player the player that this designation belongs to
     */
    public ChannelJob(final MapIndex positionTmp, final BlockType blockTypeTmp, final RegionMap mapTmp,
            final IPlayer player) {
        super(player);
        position = positionTmp;
        blockType = blockTypeTmp;
        map = mapTmp;
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

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (miner != null) {
            miner.releaseLock();
        }
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
        protected void doFinalActions() {
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
            super(position, miner, false, ChannelJob.this);
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
        protected void doFinalActions() {
            MapIndex downPosition = position.add(0, 0, -1);
            String minedItemTypeName = map.getBlock(downPosition).itemMined;
            if (minedItemTypeName != null) {
                ItemType minedItemType = ItemTypeManager.getInstance().getItemType(minedItemTypeName);
                Item minedItem = ItemTypeManager.getInstance().createItem(downPosition, minedItemType, getPlayer());
                getPlayer().getStockManager().addItem(minedItem);
            }
            map.channelBlock(downPosition, blockType);
            miner.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            miner.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
