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

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.WalkMoveComponent;
import simulation.item.Item;
import simulation.map.MapIndex;
import simulation.room.Room;

/**
 * The Class EatDrinkJob.
 */
public class EatDrinkJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -559977291069519592L;

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** The look for food. */
        LOOK_FOR_FOOD,
        /** The waiting for dwarf. */
        WAITING_FOR_DWARF,
        /** The haul. */
        HAUL,
        /** The consume. */
        CONSUME, WALK_TO_CHAIR
    }

    /** The state of this job. */
    private State state = State.LOOK_FOR_FOOD;

    /** The haul job. */
    private HaulJob haulJob;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** Amount of time to spend eating/drinking (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** True if the job is to eat, false if the job is to drink. */
    private final boolean eat;

    /** The dwarf doing this job. */
    private final Dwarf dwarf;

    /** Is this job done. */
    private boolean done = false;

    /** The food item. */
    private Item foodItem;

    /** The chair the dwarf will sit at while eating. */
    private Item chair;

    /** The table the dwarf will eat at. */
    private Item table;

    /** The walk component. */
    private WalkMoveComponent walkComponent;

    /**
     * Instantiates a new eat drink job.
     * @param dwarfTmp the dwarf
     * @param eatTmp the eat
     */
    public EatDrinkJob(final Dwarf dwarfTmp, final boolean eatTmp) {
        Logger.getInstance().log(this, "New eat drink job");
        eat = eatTmp;
        dwarf = dwarfTmp;
    }

    @Override
    public String getStatus() {
        String str;
        if (eat) {
            str = "food";
        } else {
            str = "drink";
        }

        switch (state) {
        case LOOK_FOR_FOOD:
            return "Looking for " + str;
        case WAITING_FOR_DWARF:
            return "Waiting for dwarf to become free";
        case HAUL:
            return "Hauling " + str;
        case CONSUME:
            return "Consuming " + str;
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Eat/drink job was interrupted");
        }

        dwarf.releaseLock();

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * Checks if is looking.
     * @return true, if is looking
     */
    public boolean isLooking() {
        return state == State.LOOK_FOR_FOOD;
    }

    @Override
    public String toString() {
        if (eat) {
            return "Eat";
        }

        return "Drink";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case LOOK_FOR_FOOD:
            if (eat) {
                foodItem = player.getStockManager().getUnusedItemFromCategory("Food");
            } else {
                foodItem = player.getStockManager().getUnusedItemFromCategory("Drink");
            }

            if (foodItem != null) {
                state = State.WAITING_FOR_DWARF;
            }
            break;

        case WAITING_FOR_DWARF:
            if (dwarf.acquireLock()) {
                Set<Room> rooms = player.getRooms();
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
                                    // They can share the table
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
                if (table != null) {
                    haulJob = new HaulJob(dwarf, foodItem, player.getStockManager(), table.getPosition());
                    state = State.HAUL;
                } else {
                    // TODO: haul to random location, and become sad or eat under a tree, become less sad
                    wasteTimeJob = new WasteTimeJob(dwarf, DURATION);
                    state = State.CONSUME;
                }
            }
            break;

        case HAUL:
            haulJob.update(player, region);

            if (haulJob.isDone()) {
                walkComponent = dwarf.walkToPosition(chair.getPosition(), false);
                state = State.WALK_TO_CHAIR;
            }
            break;

        case WALK_TO_CHAIR:
            if (walkComponent.isNoPath()) {
                interrupt("No path to item");
                return;
            }
            if (walkComponent.isArrived()) {
                wasteTimeJob = new WasteTimeJob(dwarf, DURATION);
                state = State.CONSUME;
            }
            break;

        case CONSUME:
            wasteTimeJob.update(player, region);

            if (wasteTimeJob.isDone()) {
                foodItem.setRemove();

                dwarf.releaseLock();

                if (eat) {
                    dwarf.getEatDrink().eat();
                } else {
                    dwarf.getEatDrink().drink();
                }

                if (chair != null) {
                    chair.setUsed(false);
                }

                done = true;
            }
            break;

        default:
            break;
        }
    }
}
