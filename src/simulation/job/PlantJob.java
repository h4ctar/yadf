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

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.IMovementComponent;
import simulation.character.component.ISkillComponent;
import simulation.character.component.StillMovementComponent;
import simulation.farm.FarmPlot;
import simulation.item.Item;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;

/**
 * The Class PlantJob.
 */
public class PlantJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -2033952601756317716L;

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** The start. */
        START,
        /** The haul. */
        HAUL,
        /** The plant. */
        PLANT
    }

    /** The labor type required to plant a crop. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Farming");

    /** The state. */
    private State state = State.START;

    /** The dwarf. */
    private Dwarf dwarf;

    /** The need to release lock. */
    private boolean needToReleaseLock;

    /** The haul job. */
    private HaulJob haulJob;

    /** The done. */
    private boolean done = false;

    /** How many simulation steps to plant. */
    private static final long PLANTING_DURATION = 2 * Region.SIMULATION_STEPS_PER_HOUR;

    /** The simulation steps. */
    private int simulationSteps = 0;

    /** The seed. */
    private final Item seed;

    /** The farm plot. */
    private final FarmPlot farmPlot;

    /**
     * Instantiates a new plant job.
     * @param seedTmp the seed
     * @param farmPlotTmp the farm plot
     */
    public PlantJob(final Item seedTmp, final FarmPlot farmPlotTmp) {
        seed = seedTmp;
        farmPlot = farmPlotTmp;
    }

    @Override
    public String getStatus() {
        switch (state) {
        case START:
            return "Waiting for farmer";
        case HAUL:
            return "Hauling seed to farm plot";
        case PLANT:
            return "Planting seed in farm plot";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);
        if (dwarf != null) {
            if (needToReleaseLock) {
                dwarf.releaseLock();
            }
        }
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Plant crop";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case START:
            if (dwarf == null) {
                dwarf = player.getIdleDwarf(REQUIRED_LABOR);
                needToReleaseLock = true;
            }
            if (dwarf == null) {
                return;
            }
            haulJob = new HaulJob(dwarf, seed, player.getStockManager(), farmPlot.getPosition());
            player.getJobManager().addJob(haulJob);
            state = State.HAUL;
            break;

        case HAUL:
            if (haulJob.isDone()) {
                dwarf.setComponent(IMovementComponent.class, new StillMovementComponent());
                simulationSteps = 0;
                state = State.PLANT;
            }
            break;

        case PLANT:
            simulationSteps++;
            if (simulationSteps > PLANTING_DURATION) {
                seed.setRemove();
                if (needToReleaseLock) {
                    dwarf.releaseLock();
                }
                dwarf.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
                done = true;
            }
            break;

        default:
            break;
        }
    }
}
