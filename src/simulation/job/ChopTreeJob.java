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
import simulation.Tree;
import simulation.character.Dwarf;
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
import simulation.map.MapIndex;

/**
 * The Class ChopTreeJob.
 */
public class ChopTreeJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -7689577169283098909L;

    /** Amount of time to spend chopping down the tree (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Wood cutting");

    /** The tree. */
    private final Tree tree;

    /** The dwarf. */
    private Dwarf lumberjack;

    /**
     * Instantiates a new chop tree job.
     * @param treeTmp the tree
     * @param player the player
     */
    public ChopTreeJob(final Tree treeTmp, final IPlayer player) {
        super(player);
        tree = treeTmp;
        setJobState(new LookingForLumberjackState());
    }

    @Override
    public MapIndex getPosition() {
        return tree.getPosition();
    }

    @Override
    public String toString() {
        return "Chop down tree";
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (lumberjack != null) {
            lumberjack.releaseLock();
        }
    }

    /**
     * The looking for lumberjack job state.
     */
    private class LookingForLumberjackState extends LookingForDwarfState {

        /**
         * Constructor.
         */
        public LookingForLumberjackState() {
            super(REQUIRED_LABOR, ChopTreeJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            lumberjack = getDwarf();
        }

        @Override
        public IJobState getNextState() {
            return new WalkToTreeState();
        }
    }

    /**
     * The walk to channel site job state.
     */
    private class WalkToTreeState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToTreeState() {
            super(tree.getPosition(), lumberjack, false, ChopTreeJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new ChopTreeState();
        }
    }

    /**
     * Chop tree job state.
     */
    private class ChopTreeState extends WasteTimeState {

        /**
         * Constructor.
         */
        public ChopTreeState() {
            super(DURATION, lumberjack, ChopTreeJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            if (tree == null || tree.isDeleted()) {
                interrupt("Tree missing");
                return;
            }
            ItemType itemType = ItemTypeManager.getInstance().getItemType("Log");
            Item log = ItemTypeManager.getInstance().createItem(tree.getPosition(), itemType, getPlayer());
            getPlayer().getStockManager().addItem(log);
            tree.delete();
            lumberjack.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            lumberjack.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
