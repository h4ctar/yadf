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
import simulation.IDwarfManagerListener;
import simulation.IPlayer;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.character.component.ICharacterComponentListener;
import simulation.character.component.IInventoryComponent;
import simulation.character.component.IMovementComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.IContainer;
import simulation.item.IStockManagerListener;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;

/**
 * The Class HaulJob.
 * 
 * A job which hauls an item to some place.
 */
public class HaulJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -1705328544980248473L;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Hauling");

    /** The current state of the job. */
    private IJobState state;

    /** The character that will haul the item. */
    private GameCharacter character;

    /** The item. */
    private Item item;

    /** If not null, the item should be stored here. */
    private final IContainer container;

    /** The drop position. */
    private final MapIndex dropPosition;

    /** The job is done. */
    private boolean done = false;

    /** The lock on the dwarf needs to be released, it should only be released if we acquired the dwarf. */
    private boolean needToReleaseLock = false;

    /** The item type. */
    private final ItemType itemType;

    /** The player that this job is for. */
    private final IPlayer player;

    /**
     * Instantiates a new haul job, start with dwarf and item.
     * @param characterTmp the character
     * @param itemTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     */
    public HaulJob(final GameCharacter characterTmp, final Item itemTmp, final IContainer containerTmp,
            final MapIndex dropPositionTmp) {
        item = itemTmp;
        container = containerTmp;
        dropPosition = dropPositionTmp;
        character = characterTmp;
        itemType = item.getType();
        player = character.getPlayer();
        state = new WalkToItemState();
    }

    /**
     * Instantiates a new haul job, start with item.
     * @param itemTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     * @param playerTmp the player
     */
    public HaulJob(final Item itemTmp, final IContainer containerTmp, final MapIndex dropPositionTmp,
            final IPlayer playerTmp) {
        item = itemTmp;
        container = containerTmp;
        dropPosition = dropPositionTmp;
        player = playerTmp;
        itemType = item.getType();
        state = new LookingForDwarfState();
    }

    /**
     * Instantiates a new haul job.
     * @param itemTypeTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     * @param playerTmp the player
     */
    public HaulJob(final ItemType itemTypeTmp, final IContainer containerTmp, final MapIndex dropPositionTmp,
            final IPlayer playerTmp) {
        container = containerTmp;
        dropPosition = dropPositionTmp;
        itemType = itemTypeTmp;
        player = playerTmp;
        item = player.getStockManager().getUnusedItem(itemType.name);
        if (item == null) {
            state = new LookingForItemState();
        } else {
            character = player.getDwarfManager().getIdleDwarf(REQUIRED_LABOR);
            if (character == null) {
                state = new LookingForDwarfState();
            } else {
                state = new WalkToItemState();
            }
        }
    }

    /**
     * Gets the drop position.
     * @return the drop position
     */
    public MapIndex getDropPosition() {
        return dropPosition;
    }

    /**
     * Gets the item.
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    @Override
    public String getStatus() {
        return state.toString();
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);
        state.cleanUp();
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Hauling " + item.getType().name;
    }

    @Override
    public void update(final Player playerTmp, final Region regionTmp) {

    }

    /**
     * Interface for a job state.
     */
    private interface IJobState {

        /**
         * Clean up the state, will be called when the job is interrupted.
         */
        void cleanUp();
    }

    /**
     * The job is looking for an item.
     */
    private class LookingForItemState implements IJobState, IStockManagerListener {

        /**
         * Constructor.
         */
        public LookingForItemState() {
            player.getStockManager().addListener(itemType, this);
        }

        @Override
        public String toString() {
            return "Looking for item";
        }

        @Override
        public void cleanUp() {
            player.getStockManager().removeListener(this);
        }

        @Override
        public void itemNowAvailable(final Item availableItem) {
            if (!availableItem.isUsed()) {
                availableItem.setUsed(true);
                item = availableItem;
                if (character == null) {
                    character = player.getDwarfManager().getIdleDwarf(REQUIRED_LABOR);
                }
                if (character == null) {
                    state = new LookingForDwarfState();
                } else {
                    state = new WalkToItemState();
                }
                cleanUp();
            }
        }
    }

    /**
     * The job is looking for a dwarf.
     */
    private class LookingForDwarfState implements IJobState, IDwarfManagerListener {

        /**
         * Constructor.
         */
        public LookingForDwarfState() {
            player.getDwarfManager().addListener(this);
        }

        @Override
        public String toString() {
            return "Looking for dwarf";
        }

        @Override
        public void cleanUp() {
            player.getDwarfManager().removeListener(this);
        }

        @Override
        public void dwarfAdded(final Dwarf dwarf) {
            acquireDwarf(dwarf);
        }

        @Override
        public void dwarfRemoved(final Dwarf dwarf) {
            // do nothing
        }

        @Override
        public void dwarfNowIdle(final Dwarf dwarf) {
            acquireDwarf(dwarf);
        }

        /**
         * Attempt to acquire the dwarf, and transition to next state if successful.
         * @param dwarf the dwarf to acquire
         */
        private void acquireDwarf(final Dwarf dwarf) {
            if (dwarf.acquireLock()) {
                character = dwarf;
                needToReleaseLock = true;
                state = new WalkToItemState();
                cleanUp();
            }
        }
    }

    /**
     * The dwarf is walking to the item.
     */
    private class WalkToItemState implements IJobState, ICharacterComponentListener {

        /** The walk component. */
        private final WalkMovementComponent walkComponent;

        /**
         * Constructor.
         */
        public WalkToItemState() {
            walkComponent = new WalkMovementComponent(item.getPosition(), false);
            walkComponent.addListener(this);
            character.setComponent(IMovementComponent.class, walkComponent);
        }

        @Override
        public String toString() {
            return "Walking to item";
        }

        @Override
        public void cleanUp() {
            walkComponent.removeListener(this);
        }

        @Override
        public void componentChanged() {
            if (walkComponent.isArrived()) {
                character.getComponent(IInventoryComponent.class).pickupHaulItem(item);
                player.getStockManager().removeItem(item);
                state = new WalkToDropState();
                cleanUp();
            } else if (walkComponent.isNoPath()) {
                interrupt("No path to item");
            }
        }
    }

    /**
     * The dwarf is walking to the drop.
     */
    private class WalkToDropState implements IJobState, ICharacterComponentListener {

        /** The walk component. */
        private final WalkMovementComponent walkComponent;

        /**
         * Constructor.
         */
        public WalkToDropState() {
            walkComponent = new WalkMovementComponent(dropPosition, false);
            walkComponent.addListener(this);
            character.setComponent(IMovementComponent.class, walkComponent);
        }

        @Override
        public String toString() {
            return "Walking to drop";
        }

        @Override
        public void cleanUp() {
            walkComponent.removeListener(this);
        }

        @Override
        public void componentChanged() {
            if (walkComponent.isArrived()) {
                character.getComponent(IInventoryComponent.class).dropHaulItem(false);
                if (needToReleaseLock) {
                    character.releaseLock();
                }
                if (container != null) {
                    if (container.getRemove()) {
                        Logger.getInstance().log(this, "Can't store item, container has been removed", true);
                        player.getStockManager().addItem(item);
                    } else {
                        if (!container.addItem(item)) {
                            Logger.getInstance().log(this, "Can't store item, container didn't accept it", true);
                            player.getStockManager().addItem(item);
                        }
                    }
                }
                done = true;
                cleanUp();
                notifyListeners();
            } else if (walkComponent.isNoPath()) {
                interrupt("No path to drop");
            }
        }
    }
}
