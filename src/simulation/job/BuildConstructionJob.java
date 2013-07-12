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

import java.util.List;

import simulation.IPlayer;
import simulation.IRegion;
import simulation.character.IGameCharacter;
import simulation.character.component.ISkillComponent;
import simulation.item.IStockManager;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.jobstate.HaulItemState;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForDwarfState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.BlockType;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.map.WalkableNode;

/**
 * The Class BuildConstructionJob.
 */
public class BuildConstructionJob extends AbstractJob {

    /** Amount of time to spend building (simulation steps). */
    private static final long BUILD_DURATION = IRegion.SIMULATION_STEPS_PER_HOUR * 2;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Building");

    /** The item type that is required as a resource to build the construction. */
    private static final ItemType BUILDING_MATERIAL_TYPE = ItemTypeManager.getInstance().getItemType("Rock");

    /** The position. */
    private final MapIndex position;

    /** The construction type. */
    private final BlockType constructionType;

    /** The builder dwarf. */
    private IGameCharacter builder;

    /** The building material. */
    private Item rock;

    /** The map that the construction will be built in. */
    final RegionMap map;

    /**
     * Instantiates a new build construction job.
     * @param positionTmp the position
     * @param constructionTypeTmp the construction type
     * @param mapTmp the map that the construction will be built in
     * @param player the player that this job belongs to
     */
    public BuildConstructionJob(final MapIndex positionTmp, final BlockType constructionTypeTmp,
            final RegionMap mapTmp, final IPlayer player) {
        super(player);
        position = positionTmp;
        constructionType = constructionTypeTmp;
        map = mapTmp;
        setJobState(new HaulBuildingMaterialsState());
    }

    @Override
    public String toString() {
        return "Build " + constructionType;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (builder != null) {
            builder.setFree();
        }
        if (rock != null) {
            rock.setUsed(false);
        }
    }

    /**
     * The haul building materials job state.
     */
    private class HaulBuildingMaterialsState extends HaulItemState {

        /**
         * Constructor.
         */
        public HaulBuildingMaterialsState() {
            super(BUILDING_MATERIAL_TYPE, position, getPlayer().getComponent(IStockManager.class),
                    BuildConstructionJob.this);
        }

        @Override
        public void doFinalActions() {
            rock = getItem();
        }

        @Override
        public IJobState getNextState() {
            return new LookingForBuilderState();
        }
    }

    /**
     * The looking for builder job state.
     */
    private class LookingForBuilderState extends LookingForDwarfState {

        /**
         * Constructor.
         */
        public LookingForBuilderState() {
            super(REQUIRED_LABOR, BuildConstructionJob.this);
        }

        @Override
        public void doFinalActions() {
            builder = getDwarf();
        }

        @Override
        public IJobState getNextState() {
            return new WalkToBuildingSiteState();
        }
    }

    /**
     * The walk to building site job state.
     */
    private class WalkToBuildingSiteState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToBuildingSiteState() {
            super(position, builder, false, BuildConstructionJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new BuildState();
        }
    }

    /**
     * The build job state.
     */
    private class BuildState extends WasteTimeState {

        /**
         * Constructor.
         */
        public BuildState() {
            super(BUILD_DURATION, builder, BuildConstructionJob.this);
        }

        @Override
        public void doFinalActions() {
            rock.delete();
            // Move items away from build area
            for (Item item : getPlayer().getComponent(IStockManager.class).getItems()) {
                if (item.getPosition().equals(position)) {
                    List<WalkableNode> adjacencies = map.getAdjacencies(position);

                    if (!adjacencies.isEmpty()) {
                        item.setPosition(adjacencies.get(0));
                    }
                }
            }
            map.setBlock(position, constructionType);
            builder.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
            builder.setFree();
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
