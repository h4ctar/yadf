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

import simulation.character.IGameCharacter;
import simulation.character.component.IInventoryComponent;
import simulation.item.Item;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.map.MapIndex;

/**
 * The Class PickupToolJob.
 */
public class PickupToolJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -8533807757453770766L;

    /** The dwarf. */
    private final IGameCharacter character;

    /** The tool. */
    private final Item tool;

    /**
     * Instantiates a new pickup tool job.
     * @param characterTmp the character
     * @param toolTmp the tool
     */
    public PickupToolJob(final IGameCharacter characterTmp, final Item toolTmp) {
        super(characterTmp.getPlayer());
        character = characterTmp;
        tool = toolTmp;
        tool.setUsed(true);
        setJobState(new WaitingForDwarfState());
    }

    @Override
    public String toString() {
        return "Pickup tool";
    }

    @Override
    public MapIndex getPosition() {
        return tool.getPosition();
    }

    /**
     * The waiting for dwarf job state.
     */
    private class WaitingForDwarfState extends simulation.job.jobstate.WaitingForDwarfState {

        /**
         * Constructor.
         */
        public WaitingForDwarfState() {
            super(character, PickupToolJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new WalkToToolState();
        }
    }

    /**
     * The walk to tool job state.
     */
    private class WalkToToolState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToToolState() {
            super(tool.getPosition(), character, false, PickupToolJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            character.getComponent(IInventoryComponent.class).pickupTool(tool);
            character.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
