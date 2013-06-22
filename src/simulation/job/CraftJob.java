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
import simulation.item.Item;
import simulation.item.ItemTypeManager;
import simulation.job.jobstate.HaulResourcesState;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForDwarfState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.recipe.Recipe;
import simulation.workshop.Workshop;

/**
 * The Class CraftJob.
 */
public class CraftJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 8969989694678543875L;

    /** How long it takes to craft the item. */
    private static final long CRAFT_DURATION = 2 * Region.SIMULATION_STEPS_PER_HOUR;

    /** The workshop. */
    private final Workshop workshop;

    /** The recipe. */
    private final Recipe recipe;

    /** The dwarf. */
    private Dwarf crafter;

    /** The required labor type for this job. */
    private final LaborType requiredLabor;

    /**
     * Instantiates a new craft job.
     * @param workshopTmp the workshop
     * @param recipeTmp the recipe
     * @param player the player that this job belongs to
     */
    public CraftJob(final Workshop workshopTmp, final Recipe recipeTmp, final IPlayer player) {
        super(player);
        workshop = workshopTmp;
        recipe = recipeTmp;
        workshop.setOccupied(true);
        requiredLabor = LaborTypeManager.getInstance().getLaborType(recipe.skill);
        setJobState(new HaulCraftingMaterialsState());
    }

    @Override
    public String toString() {
        return "Crafting " + recipe.itemType.name;
    }

    @Override
    public MapIndex getPosition() {
        return workshop.getPosition();
    }

    /**
     * The haul crafting materials job state.
     */
    private class HaulCraftingMaterialsState extends HaulResourcesState {

        /**
         * Constructor.
         */
        public HaulCraftingMaterialsState() {
            super(recipe.resources, workshop.getPosition(), CraftJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new LookingForCrafterState();
        }
    }

    /**
     * The looking for crafter job state.
     */
    private class LookingForCrafterState extends LookingForDwarfState {

        /**
         * Constructor.
         */
        public LookingForCrafterState() {
            super(requiredLabor, CraftJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            crafter = getDwarf();
        }

        @Override
        public IJobState getNextState() {
            return new WalkToWorkshopState();
        }
    }

    /**
     * The walk to workshop job state.
     */
    private class WalkToWorkshopState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToWorkshopState() {
            super(workshop.getPosition(), crafter, CraftJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new ConstructCraftState();
        }
    }

    /**
     * The construct craft job state.
     */
    private class ConstructCraftState extends WasteTimeState {

        /**
         * Constructor.
         */
        public ConstructCraftState() {
            super(CRAFT_DURATION, crafter, CraftJob.this);
        }

        @Override
        public void transitionOutOf() {
            super.transitionOutOf();
            for (int i = 0; i < recipe.quantity; i++) {
                Item newItem = ItemTypeManager.getInstance().createItem(workshop.getPosition(), recipe.itemType,
                        getPlayer());
                getPlayer().getStockManager().addItem(newItem);
            }
            workshop.setOccupied(false);
            if (recipe.skill != null) {
                crafter.getComponent(ISkillComponent.class).increaseSkillLevel(requiredLabor);
            }
            crafter.releaseLock();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
