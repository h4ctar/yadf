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

import java.util.Set;

import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.IEatDrinkComponent;
import simulation.item.Item;
import simulation.job.jobstate.HaulItemState;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.LookingForItemState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.map.MapIndex;
import simulation.room.Room;

/**
 * The Class EatDrinkJob.
 */
public class EatDrinkJob extends AbstractJob {

    /** Amount of time to spend eating/drinking (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** True if the job is to eat, false if the job is to drink. */
    private final boolean eat;

    /** The dwarf doing this job. */
    private final Dwarf dwarf;

    /** Food or drink item. */
    private Item foodDrinkItem;

    /** The chair the dwarf will sit at while eating. */
    private Item chair;

    /** The table the dwarf will eat at. */
    private Item table;

    /**
     * Instantiates a new eat drink job.
     * @param dwarfTmp the dwarf
     * @param eatTmp the eat
     */
    public EatDrinkJob(final Dwarf dwarfTmp, final boolean eatTmp) {
        super(dwarfTmp.getPlayer());
        eat = eatTmp;
        dwarf = dwarfTmp;
        setJobState(new LookingForFoodDrinkState());
    }

    @Override
    public String toString() {
        if (eat) {
            return "Eat";
        }
        return "Drink";
    }

    @Override
    public MapIndex getPosition() {
        return dwarf.getPosition();
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (dwarf != null) {
            dwarf.releaseLock();
        }
        if (chair != null) {
            chair.setUsed(false);
        }
        if (foodDrinkItem != null) {
            foodDrinkItem.setUsed(false);
        }
    }

    /**
     * The looking for food or drink job state.
     */
    private class LookingForFoodDrinkState extends LookingForItemState {

        /**
         * Constructor.
         */
        public LookingForFoodDrinkState() {
            super(eat ? "Food" : "Drink", false, false, EatDrinkJob.this);
        }

        @Override
        protected void doFinalActions() {
            foodDrinkItem = getItem();
        }

        @Override
        public IJobState getNextState() {
            return new WaitingForDwarfState();
        }
    }

    /**
     * The waiting for dwarf state.
     */
    private class WaitingForDwarfState extends simulation.job.jobstate.WaitingForDwarfState {

        /**
         * Constructor.
         */
        public WaitingForDwarfState() {
            super(dwarf, EatDrinkJob.this);
        }

        @Override
        protected void doFinalActions() {
            Set<Room> rooms = getPlayer().getRooms();
            for (Room room : rooms) {
                if (room.getType().equals("Dining room")) {
                    Set<Item> tables = room.getUnusedItems("Table");
                    Set<Item> chairs = room.getUnusedItems("Chair");
                    for (Item tableTmp : tables) {
                        for (Item chairTmp : chairs) {
                            MapIndex chairPos = chairTmp.getPosition();
                            MapIndex tablePos = tableTmp.getPosition();
                            boolean horizontal = chairPos.x == tablePos.x
                                    && (chairPos.y == tablePos.y - 1 || chairPos.y == tablePos.y + 1);
                            boolean vertical = chairPos.x == tablePos.x && chairPos.y == tablePos.y
                                    && (chairPos.x == tablePos.x - 1 || chairPos.x == tablePos.x + 1);
                            if (horizontal || vertical) {
                                chair = chairTmp;
                                table = tableTmp;
                                chair.setUsed(true);
                                break;
                            }
                        }
                        if (table != null) {
                            break;
                        }
                    }
                }
                if (table != null) {
                    break;
                }
            }
        }

        @Override
        public IJobState getNextState() {
            IJobState nextState;
            if (table != null) {
                nextState = new HaulFoodDrinkToTableState();
            } else {
                nextState = new WalkToFoodDrinkState();
            }
            return nextState;
        }
    }

    /**
     * The walk to food or drink job state.
     */
    private class WalkToFoodDrinkState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToFoodDrinkState() {
            super(foodDrinkItem.getPosition(), dwarf, false, EatDrinkJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new ConsumeFoodDrinkState();
        }
    }

    /**
     * The haul food or drink job state.
     * 
     * Hauls the food or drink item to where it will be eaten; the table.
     */
    private class HaulFoodDrinkToTableState extends HaulItemState {

        /**
         * Constructor.
         */
        public HaulFoodDrinkToTableState() {
            super(dwarf, foodDrinkItem, table.getPosition(), getPlayer().getStockManager(), EatDrinkJob.this);
        }

        @Override
        public IJobState getNextState() {
            IJobState nextState;
            if (chair != null) {
                nextState = new WalkToChairState();
            } else {
                nextState = new ConsumeFoodDrinkState();
            }
            return nextState;
        }
    }

    /**
     * The walk to chair job state.
     */
    private class WalkToChairState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToChairState() {
            super(chair.getPosition(), dwarf, false, EatDrinkJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new ConsumeFoodDrinkState();
        }
    }

    /**
     * The consume food or drink job state.
     */
    private class ConsumeFoodDrinkState extends WasteTimeState {

        /**
         * Constructor.
         */
        public ConsumeFoodDrinkState() {
            super(DURATION, dwarf, EatDrinkJob.this);
        }

        @Override
        protected void doFinalActions() {
            foodDrinkItem.delete();
            dwarf.releaseLock();
            if (eat) {
                dwarf.getComponent(IEatDrinkComponent.class).eat();
            } else {
                dwarf.getComponent(IEatDrinkComponent.class).drink();
            }
            if (chair != null) {
                chair.setUsed(false);
            }
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
