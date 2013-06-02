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
package simulation.farm;

import simulation.Player;
import simulation.Region;
import simulation.item.Item;
import simulation.job.HarvestJob;
import simulation.job.IJob;
import simulation.job.PlantJob;
import simulation.job.TillJob;
import simulation.map.MapIndex;

/**
 * The Class FarmPlot.
 */
public class FarmPlot {

    /**
     * The State of the job.
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

    /** The state. */
    private State state = State.START;

    /** The position. */
    private final MapIndex position;

    /** The job. */
    private IJob job;

    /** The Constant growDuration. */
    private static final long GROW_DURATION = Region.SIMULATION_STEPS_PER_WEEK;

    /** The simulation steps. */
    private int simulationSteps;

    /**
     * Instantiates a new farm plot.
     * 
     * @param positionTmp the position
     */
    public FarmPlot(final MapIndex positionTmp) {
        position = positionTmp;
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
     * 
     * @param player the player
     */
    public void update(final Player player) {
        switch (state) {
        case START:
            job = new TillJob(this);
            player.getJobManager().addJob(job);

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
                Item seed = player.getStockManager().getUnusedItem("Seed");
                if (seed != null) {
                    job = new PlantJob(seed, this);
                    player.getJobManager().addJob(job);

                }
            } else if (job.isDone()) {
                simulationSteps = 0;
                state = State.GROW;
            }
            break;

        case GROW:
            simulationSteps++;
            if (simulationSteps > GROW_DURATION) {
                job = new HarvestJob(this);
                player.getJobManager().addJob(job);
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
