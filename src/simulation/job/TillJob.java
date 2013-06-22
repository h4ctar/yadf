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
import simulation.character.Dwarf;
import simulation.character.component.ISkillComponent;
import simulation.farm.FarmPlot;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForDwarfState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;

/**
 * The Class TillJob.
 */
public class TillJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -8463713853261562676L;

    /** The Constant tillDuration. */
    private static final long TILL_DURATION = 2 * Region.SIMULATION_STEPS_PER_HOUR;

    /** The required labor. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Farming");

    /** The farm plot. */
    private final FarmPlot farmPlot;

    /** The farmer dwarf. */
    private Dwarf farmer;

    /**
     * Instantiates a new till job.
     * @param farmPlotTmp the farm plot
     * @param player the player that this job belongs to
     */
    public TillJob(final FarmPlot farmPlotTmp, final IPlayer player) {
        super(player);
        farmPlot = farmPlotTmp;
        setJobState(new LookingForFarmerState());
    }

    @Override
    public String toString() {
        return "Till farm plot";
    }

    @Override
    public MapIndex getPosition() {
        return farmPlot.getPosition();
    }

    /**
     * The waiting for farmer job state.
     */
    private class LookingForFarmerState extends LookingForDwarfState {

        /**
         * Constructor.
         */
        public LookingForFarmerState() {
            super(REQUIRED_LABOR, TillJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            farmer = getDwarf();
        }

        @Override
        public IJobState getNextState() {
            return new WalkToCropState();
        }
    }

    /**
     * The walk to crop state.
     */
    private class WalkToCropState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToCropState() {
            super(farmPlot.getPosition(), farmer, TillJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new TillCropState();
        }
    }

    /**
     * The harvest crop state.
     */
    private class TillCropState extends WasteTimeState {

        /**
         * Constructor.
         */
        public TillCropState() {
            super(TILL_DURATION, farmer, TillJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            farmer.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            farmer.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
