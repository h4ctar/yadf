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
import simulation.character.GameCharacter;
import simulation.item.Item;

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
        CONSUME
    }

    /** The state of this job. */
    private State state = State.LOOK_FOR_FOOD;

    /** The haul job. */
    private HaulJob haulJob = null;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** Amount of time to spend eating/drinking (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** True if the job is to eat, false if the job is to drink. */
    private final boolean eat;

    /** The character that ordered this job. */
    private final GameCharacter character;

    /** Is this job done. */
    private boolean done = false;

    /** The food item. */
    private Item foodItem;

    /**
     * Instantiates a new eat drink job.
     * 
     * @param characterTmp the character
     * @param eatTmp the eat
     */
    public EatDrinkJob(final GameCharacter characterTmp, final boolean eatTmp) {
        Logger.getInstance().log(this, "New eat drink job");
        eat = eatTmp;
        character = characterTmp;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Eat/drink job was interrupted");
        }

        character.releaseLock();

        done = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * Checks if is looking.
     * 
     * @return true, if is looking
     */
    public boolean isLooking() {
        return state == State.LOOK_FOR_FOOD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (eat) {
            return "Eat";
        }

        return "Drink";
    }

    /**
     * {@inheritDoc}
     */
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
            if (character.acquireLock()) {
                // TODO: this should find the position of a table and chair.
                haulJob = new HaulJob(character, foodItem, player.getStockManager(), foodItem.getPosition());
                state = State.HAUL;
            }
            break;

        case HAUL:
            haulJob.update(player, region);

            if (haulJob.isDone()) {
                wasteTimeJob = new WasteTimeJob(character, DURATION);
                state = State.CONSUME;
            }
            break;

        case CONSUME:
            wasteTimeJob.update(player, region);

            if (wasteTimeJob.isDone()) {
                foodItem.setRemove();

                character.releaseLock();

                if (eat) {
                    character.getEatDrink().eat();
                } else {
                    character.getEatDrink().drink();
                }

                done = true;
            }
            break;

        default:
            break;
        }
    }
}
