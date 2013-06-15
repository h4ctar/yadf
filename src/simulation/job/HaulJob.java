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
import simulation.AbstractGameObject;
import simulation.IPlayerListener;
import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.character.component.IInventoryComponent;
import simulation.character.component.IMovementComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.IContainer;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.stock.IStockManagerListener;

/**
 * The Class HaulJob.
 * 
 * A job which hauls an item to some place.
 */
public class HaulJob extends AbstractJob implements IStockManagerListener, IPlayerListener {

    /** All the possible job states. */
    enum State {
        /** The looking for item. */
        LOOKING_FOR_ITEM,
        /** The waiting for the/a dwarf to become free to haul the item. */
        WAITING_FOR_DWARF,
        /** The dwarf is walking to the item. */
        GOTO_ITEM,
        /** The dwarf is hauling the item to the drop location. */
        GOTO_DROP
    }

    /** The serial version UID. */
    private static final long serialVersionUID = -1705328544980248473L;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Hauling");

    /** The current state of the job. */
    private State state;

    /** The character that will haul the item. */
    private GameCharacter character;

    /** The item. */
    private Item item = null;

    /** If not null, the item should be stored here. */
    private final IContainer container;

    /** The drop position. */
    private final MapIndex dropPosition;

    /** The walk component. */
    private WalkMovementComponent walkComponent;

    /** The job is done. */
    private boolean done = false;

    /** The lock on the dwarf needs to be released. */
    private boolean needToReleaseLock = false;

    /** The item type. */
    private final ItemType itemType;

    /**
     * Instantiates a new haul job.
     * @param characterTmp the character
     * @param itemTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     */
    public HaulJob(final GameCharacter characterTmp, final Item itemTmp, final IContainer containerTmp,
            final MapIndex dropPositionTmp) {
        this(itemTmp, containerTmp, dropPositionTmp);
        character = characterTmp;
        state = State.WAITING_FOR_DWARF;
        character.getPlayer().addListener(this);
    }

    /**
     * Instantiates a new haul job.
     * @param itemTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     */
    public HaulJob(final Item itemTmp, final IContainer containerTmp, final MapIndex dropPositionTmp) {
        item = itemTmp;
        container = containerTmp;
        dropPosition = dropPositionTmp;
        itemType = item.getType();
        state = State.WAITING_FOR_DWARF;
    }

    /**
     * Instantiates a new haul job.
     * @param itemTypeTmp the item
     * @param containerTmp the container to put the item in
     * @param dropPositionTmp the drop position
     */
    public HaulJob(final ItemType itemTypeTmp, final IContainer containerTmp, final MapIndex dropPositionTmp) {
        container = containerTmp;
        dropPosition = dropPositionTmp;
        itemType = itemTypeTmp;
        state = State.LOOKING_FOR_ITEM;
        character.getPlayer().getStockManager().addListener(itemType, this);
    }

    private void updateLookingForItem() {

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
        if (character == null) {
            return "Not assigned";
        }
        switch (state) {
        case LOOKING_FOR_ITEM:
            return "Looking for item";
        case WAITING_FOR_DWARF:
            return "Waiting for dwarf";
        case GOTO_ITEM:
            return "Walking to item";
        case GOTO_DROP:
            return "Walking to drop location";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);
        // Drop the item
        if (character != null) {
            character.getComponent(IInventoryComponent.class).dropHaulItem(true);
            if (needToReleaseLock) {
                character.releaseLock();
            }
        }
        if (item != null) {
            item.setUsed(false);
        }
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
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }
        switch (state) {
        case LOOKING_FOR_ITEM:
            if (item == null) {
                item = player.getStockManager().getUnusedItem(itemType.name);
            }
            if (item != null) {
                Logger.getInstance().log(this, "Item found, should now look for dwarf");
                item.setUsed(true);
                state = State.WAITING_FOR_DWARF;
            }
            break;

        case WAITING_FOR_DWARF:
            if (character == null) {
                character = player.getIdleDwarf(REQUIRED_LABOR);
                needToReleaseLock = true;
            }
            if (character != null) {
                Logger.getInstance().log(this, "Dwarf found, should now go to item");
                walkComponent = new WalkMovementComponent(item.getPosition(), false);
                character.setComponent(IMovementComponent.class, walkComponent);
                state = State.GOTO_ITEM;
            }
            break;

        case GOTO_ITEM:
            if (walkComponent.isNoPath()) {
                interrupt("No path to item");
                return;
            }
            if (walkComponent.isArrived()) {
                Logger.getInstance().log(this, "Arrived at item, should now take it to drop");
                player.getStockManager().removeItem(item);
                walkComponent = new WalkMovementComponent(dropPosition, false);
                character.setComponent(IMovementComponent.class, walkComponent);
                character.getComponent(IInventoryComponent.class).pickupHaulItem(item);
                state = State.GOTO_DROP;
            }
            break;

        case GOTO_DROP:
            if (walkComponent.isNoPath()) {
                interrupt("No path to drop");
                return;
            }
            if (walkComponent.isArrived()) {
                Logger.getInstance().log(this, "Arrived at drop, should now drop item; job done");
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
                notifyListeners();
            }
            break;

        default:
            break;
        }
    }

    @Override
    public void stockManagerChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void playerChanged(AbstractGameObject gameObject, boolean added) {
        // TODO Auto-generated method stub

    }
}
