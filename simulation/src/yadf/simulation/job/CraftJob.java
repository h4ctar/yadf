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
package yadf.simulation.job;

import java.util.Set;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.ISkillComponent;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.job.jobstate.HaulResourcesState;
import yadf.simulation.job.jobstate.IJobState;
import yadf.simulation.job.jobstate.LookingForDwarfState;
import yadf.simulation.job.jobstate.WalkToPositionState;
import yadf.simulation.job.jobstate.WasteTimeState;
import yadf.simulation.labor.LaborType;
import yadf.simulation.labor.LaborTypeManager;
import yadf.simulation.map.MapIndex;
import yadf.simulation.recipe.Recipe;
import yadf.simulation.workshop.IWorkshop;

/**
 * The Class CraftJob.
 */
public class CraftJob extends AbstractJob {

    /** How long it takes to craft the item. */
    private static final long CRAFT_DURATION = 2 * IRegion.SIMULATION_STEPS_PER_HOUR;

    /** The workshop. */
    private final IWorkshop workshop;

    /** The recipe. */
    private final Recipe recipe;

    /** The dwarf. */
    private IGameCharacter crafter;

    /** The required labor type for this job. */
    private final LaborType requiredLabor;

    /** The resources that have been hauled to the crafting site. */
    private Set<Item> resources;

    /**
     * Instantiates a new craft job.
     * @param workshopTmp the workshop
     * @param recipeTmp the recipe
     * @param player the player that this job belongs to
     */
    public CraftJob(final IWorkshop workshopTmp, final Recipe recipeTmp, final IPlayer player) {
        super(player);
        workshop = workshopTmp;
        recipe = recipeTmp;
        workshop.setAvailable(false);
        requiredLabor = LaborTypeManager.getInstance().getLaborType(recipe.skill);
        setJobState(new HaulCraftingMaterialsState());
    }

    @Override
    public String toString() {
        String string = "Crafting ";
        if (recipe != null) {
            string += recipe.itemType.name;
        } else {
            string += "item";
        }
        return string;
    }

    @Override
    public MapIndex getPosition() {
        return workshop.getPosition();
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (crafter != null) {
            crafter.setAvailable(true);
        }
        if (resources != null) {
            for (Item resource : resources) {
                resource.setAvailable(true);
            }
        }
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
        protected void doFinalActions() {
            resources = getResources();
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
        protected void doFinalActions() {
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
            super(workshop.getPosition(), crafter, false, CraftJob.this);
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
        protected void doFinalActions() {
            for (int i = 0; i < recipe.quantity; i++) {
                Item newItem = new Item(workshop.getPosition(), recipe.itemType, getPlayer());
                getPlayer().getComponent(IStockManager.class).addItem(newItem);
            }
            workshop.setAvailable(true);
            if (recipe.skill != null) {
                crafter.getComponent(ISkillComponent.class).increaseSkillLevel(requiredLabor);
            }
            for (Item resource : resources) {
                resource.delete();
            }
            crafter.setAvailable(true);
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
