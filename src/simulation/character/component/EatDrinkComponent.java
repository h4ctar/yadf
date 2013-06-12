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
package simulation.character.component;

import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.job.EatDrinkJob;

/**
 * The Class EatDrinkComponent.
 */
public class EatDrinkComponent extends AbstractCharacterComponent {

    /** How many simulation steps before the dwarf will want to eat. */
    private static final long HUNGER_EAT_THRESHOLD = Region.SIMULATION_STEPS_PER_DAY;

    /** How many simulation steps before the dwarf start getting sick. */
    private static final long HUNGER_SICK_THRESHOLD = Region.SIMULATION_STEPS_PER_WEEK;

    /** How many simulation steps since the dwarf last drank. */
    private int thirst = 0;

    /** How many simulation steps before the dwarf will want to drink. */
    private static final long THIRST_DRINK_THRESHOLD = Region.SIMULATION_STEPS_PER_DAY;

    /** How many simulation steps before the dwarf will start getting sick. */
    private static final long THIRST_SICK_THRESHOLD = Region.SIMULATION_STEPS_PER_WEEK;

    /** How many simulation steps since the dwarf last ate. */
    private int hunger = 0;

    /** A reference to the eat job. */
    private EatDrinkJob eatJob;

    /** A reference to the drink job. */
    private EatDrinkJob drinkJob;

    /**
     * Can work.
     * @return true, if successful
     */
    public boolean canWork() {
        // Can only work if is neither eating or drinking
        return (eatJob == null || eatJob.isLooking()) && (drinkJob == null || drinkJob.isLooking());
    }

    /**
     * Drink.
     */
    public void drink() {
        thirst = 0;
    }

    /**
     * Eat.
     */
    public void eat() {
        hunger = 0;
    }

    /**
     * Gets the hunger.
     * @return the hunger
     */
    public int getHunger() {
        return (int) (hunger * 100 / HUNGER_EAT_THRESHOLD);
    }

    /**
     * Gets the thirst.
     * @return the thirst
     */
    public int getThirst() {
        return (int) (thirst * 100 / THIRST_DRINK_THRESHOLD);
    }

    /**
     * Kill.
     */
    @Override
    public void kill() {
        eatJob = null;
        drinkJob = null;
    }

    @Override
    public void update(final GameCharacter character, final Player player, final Region region) {
        hunger++;
        thirst++;

        if (hunger > HUNGER_EAT_THRESHOLD && eatJob == null) {
            eatJob = new EatDrinkJob((Dwarf) character, true);
            player.getJobManager().addJob(eatJob);
        }

        if (thirst > THIRST_DRINK_THRESHOLD && drinkJob == null) {
            drinkJob = new EatDrinkJob((Dwarf) character, false);
            player.getJobManager().addJob(drinkJob);
        }

        if (hunger > HUNGER_SICK_THRESHOLD) {
            character.getHealth().decrementHealth();
        }

        if (thirst > THIRST_SICK_THRESHOLD) {
            character.getHealth().decrementHealth();
        }

        if (eatJob != null && eatJob.isDone()) {
            eatJob = null;
        }

        if (drinkJob != null && drinkJob.isDone()) {
            drinkJob = null;
        }
    }
}
