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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.recipe.Recipe;
import simulation.workshop.Workshop;

/**
 * The Class CraftJob.
 */
public class CraftJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 8969989694678543875L;

    /**
     * The different states that this job can be in.
     */
    enum State {
        /** The waiting for resources state. */
        WAITING_FOR_RESOURCES,
        /** The haul resources state. */
        HAUL_RESOURCES,
        /** The waiting for dwarf state. */
        WAITING_FOR_DWARF,
        /** The dwarf walking to workshop state. */
        GOTO_WORKSHOP,
        /** The craft state. */
        CRAFT
    }

    /** How long it takes to craft the item. */
    private static final long CRAFT_TIME = 2 * Region.SIMULATION_STEPS_PER_HOUR;

    /** The state. */
    private State state = State.WAITING_FOR_RESOURCES;

    /** The workshop. */
    private final Workshop workshop;

    /** The recipe. */
    private final Recipe recipe;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** The done. */
    private boolean done = false;

    /** The walk component. */
    private WalkMovementComponent walkComponent;

    /** The dwarf. */
    private Dwarf dwarf;

    /** References to the haul jobs that are scheduled for materials. */
    private final List<HaulJob> haulJobs = new ArrayList<>();

    /** The required labor type for this job. */
    private final LaborType requiredLabor;

    /**
     * Instantiates a new craft job.
     * @param workshopTmp the workshop
     * @param recipeTmp the recipe
     */
    public CraftJob(final Workshop workshopTmp, final Recipe recipeTmp) {
        workshop = workshopTmp;
        recipe = recipeTmp;
        workshop.setOccupied(true);
        requiredLabor = LaborTypeManager.getInstance().getLaborType(recipe.skill);
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAITING_FOR_RESOURCES:
            return "Waiting on resources";
        case HAUL_RESOURCES:
            return "Hauling resources";
        case WAITING_FOR_DWARF:
            return "Waiting for crafter";
        case GOTO_WORKSHOP:
            return "Walking to site";
        case CRAFT:
            return "Crafting";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        workshop.setOccupied(false);

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
        return "Craft " + recipe.itemType.name;
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAITING_FOR_RESOURCES:
            for (Entry<ItemType, Integer> entry : recipe.resources.entrySet()) {
                ItemType itemType = entry.getKey();
                for (int i = 0; i < entry.getValue().intValue(); i++) {
                    HaulJob haulJob = new HaulJob(itemType, player.getStockManager(), workshop.getRandomPosition(),
                            player);
                    haulJobs.add(haulJob);
                }
            }
            state = State.HAUL_RESOURCES;
            break;

        case HAUL_RESOURCES:
            boolean resourcesDone = true;
            for (HaulJob haulJob : haulJobs) {
                haulJob.update(player, region);
                if (!haulJob.isDone()) {
                    resourcesDone = false;
                }
            }

            if (resourcesDone) {
                state = State.WAITING_FOR_DWARF;
            }
            break;

        case WAITING_FOR_DWARF:
            if (dwarf == null) {
                dwarf = player.getDwarfManager().getIdleDwarf(requiredLabor);
            }

            if (dwarf != null) {
                walkComponent = new WalkMovementComponent(workshop.getRandomPosition(), false);
                dwarf.setComponent(IMovementComponent.class, walkComponent);
                state = State.GOTO_WORKSHOP;
            }
            break;

        case GOTO_WORKSHOP:
            if (walkComponent.isNoPath()) {
                interrupt("No path to workshop");
                return;
            }

            if (walkComponent.isArrived()) {
                wasteTimeJob = new WasteTimeJob(dwarf, CRAFT_TIME);
                state = State.CRAFT;
            }
            break;

        case CRAFT:
            wasteTimeJob.update(player, region);

            if (wasteTimeJob.isDone()) {
                for (HaulJob haulJob : haulJobs) {
                    haulJob.getItem().setRemove();
                }
                for (int i = 0; i < recipe.quantity; i++) {
                    Item newItem = ItemTypeManager.getInstance().createItem(workshop.getPosition(), recipe.itemType,
                            player);
                    player.getStockManager().addItem(newItem);
                }
                workshop.setOccupied(false);
                if (recipe.skill != null) {
                    dwarf.getComponent(ISkillComponent.class).increaseSkillLevel(requiredLabor);
                }
                dwarf.releaseLock();
                done = true;
            }
            break;

        default:
            break;
        }
    }
}
