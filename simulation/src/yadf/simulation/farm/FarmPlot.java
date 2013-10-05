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
package yadf.simulation.farm;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.job.HarvestJob;
import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.PlantJob;
import yadf.simulation.job.TillJob;
import yadf.simulation.map.MapIndex;

/**
 * The Class FarmPlot.
 */
public class FarmPlot implements IFarmPlot {

    /**
     * The State of the farm plot.
     */
    public enum State {
        /** The start state. */
        START,
        /** The till state. */
        TILL,
        /** The plant state. */
        PLANT,
        /** The grow state. */
        GROW,
        /** The harvest state. */
        HARVEST
    }

    /** The Constant growDuration. */
    private static final long GROW_DURATION = IRegion.SIMULATION_STEPS_PER_WEEK;

    /** The state. */
    private State state = State.START;

    /** The position. */
    private final MapIndex position;

    /** The job. */
    private IJob job;

    /** The simulation steps. */
    private int simulationSteps;

    /** The player that the farm belongs to. */
    private final IPlayer player;

    /**
     * Instantiates a new farm plot.
     * 
     * @param positionTmp the position
     * @param playerTmp the player that this farm belongs to
     */
    FarmPlot(final MapIndex positionTmp, final IPlayer playerTmp) {
        position = positionTmp;
        player = playerTmp;
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public MapIndex getPosition() {
        return new MapIndex(position);
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * Update.
     */
    public void update() {
        // TODO: the farm should listen to the jobs
        switch (state) {
        case START:
            job = new TillJob(this, player);
            player.getComponent(IJobManager.class).addJob(job);

            state = State.TILL;
            break;

        case TILL:
            if (job.isDone()) {
                job = null;
                state = State.PLANT;
            }
            break;

        case PLANT:
            if (job == null) {
                Item seed = player.getComponent(IStockManager.class).getItem("Seed", false, false);
                if (seed != null) {
                    player.getComponent(IStockManager.class).removeItem(seed);
                    seed.setUsed(true);
                    job = new PlantJob(seed, this, player);
                    player.getComponent(IJobManager.class).addJob(job);
                }
            } else if (job.isDone()) {
                simulationSteps = 0;
                state = State.GROW;
            }
            break;

        case GROW:
            simulationSteps++;
            if (simulationSteps > GROW_DURATION) {
                job = new HarvestJob(this, player);
                player.getComponent(IJobManager.class).addJob(job);
                state = State.HARVEST;
            }
            break;

        case HARVEST:
            if (job.isDone()) {
                job = null;
                state = State.START;
            }
            break;

        default:
            break;
        }
    }
}
