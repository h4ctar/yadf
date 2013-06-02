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
import simulation.character.component.WalkMoveComponent;
import simulation.farm.FarmPlot;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;

/**
 * The Class HarvestJob.
 */
public class HarvestJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 220791158826975417L;

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** The waiting for dwarf state. */
        WAITING_FOR_DWARF,
        /** The goto state. */
        GOTO,
        /** The harvest state. */
        HARVEST
    }

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Farming");

    /** The state. */
    private State state = State.WAITING_FOR_DWARF;

    /** The farm plot. */
    private final FarmPlot farmPlot;

    /** The dwarf. */
    private Dwarf dwarf;

    /** The need to release lock. */
    private boolean needToReleaseLock;

    /** The move component. */
    private WalkMoveComponent moveComponent;

    /** The done. */
    private boolean done = false;

    /** The Constant harvestDuration. */
    private static final long HARVEST_DURATION = 2 * Region.SIMULATION_STEPS_PER_HOUR;

    /** The simulation steps. */
    private int simulationSteps = 0;

    /**
     * Instantiates a new harvest job.
     * @param farmPlotTmp the farm plot
     */
    public HarvestJob(final FarmPlot farmPlotTmp) {
        farmPlot = farmPlotTmp;
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAITING_FOR_DWARF:
            return "Waiting for farmer";
        case GOTO:
            return "Walking to the farm plot";
        case HARVEST:
            return "Tilling the farm plot";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        // Drop the item
        if (dwarf != null) {
            dwarf.beIdleMovement();

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
        return "Harvest crop";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAITING_FOR_DWARF:
            if (dwarf == null) {
                dwarf = player.getIdleDwarf(REQUIRED_LABOR);
                needToReleaseLock = true;
            }

            if (dwarf != null) {
                moveComponent = dwarf.walkToPosition(farmPlot.getPosition(), false);

                state = State.GOTO;
            }
            break;

        case GOTO:
            if (moveComponent.isNoPath()) {
                interrupt("No path to farm plot");
                return;
            }

            if (moveComponent.isArrived()) {
                dwarf.beStillMovement();
                simulationSteps = 0;
                state = State.HARVEST;
            }
            break;

        case HARVEST:
            simulationSteps++;
            if (simulationSteps > HARVEST_DURATION) {
                dwarf.beIdleMovement();

                // TODO: move the names of the item types into constants
                ItemType itemType = ItemTypeManager.getInstance().getItemType("Wheat");
                Item newItem = ItemTypeManager.getInstance().createItem(farmPlot.getPosition(), itemType, player);
                player.getStockManager().addItem(newItem);
                itemType = ItemTypeManager.getInstance().getItemType("Seed");
                newItem = ItemTypeManager.getInstance().createItem(farmPlot.getPosition(), itemType, player);
                player.getStockManager().addItem(newItem);

                if (needToReleaseLock) {
                    dwarf.releaseLock();
                }

                dwarf.getSkill().increaseSkillLevel(REQUIRED_LABOR);

                done = true;
            }
            break;

        default:
            break;
        }
    }
}