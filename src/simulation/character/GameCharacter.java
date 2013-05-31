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
package simulation.character;

import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import simulation.Entity;
import simulation.Player;
import simulation.Region;
import simulation.character.component.AttackComponent;
import simulation.character.component.EatDrinkComponent;
import simulation.character.component.HealthComponent;
import simulation.character.component.IdleMoveComponent;
import simulation.character.component.InventoryComponent;
import simulation.character.component.MoveComponent;
import simulation.character.component.StillMoveComponent;
import simulation.character.component.WalkMoveComponent;
import simulation.map.MapIndex;

/**
 * The Class GameCharacter.
 */
public class GameCharacter extends Entity {

    /** The health. */
    private final HealthComponent health;

    /** The list of listeners. */
    private final List<ICharacterListener> listeners;

    /** The move. */
    private MoveComponent move;

    /** The eat drink. */
    private final EatDrinkComponent eatDrink;

    /** The attack. */
    private AttackComponent attack;

    /** The inventory. */
    private final InventoryComponent inventory;

    /** The dead. */
    protected boolean dead;

    /** The name. */
    protected String name;

    /** The lock. */
    private boolean lock;

    /**
     * Instantiates a new game character.
     * 
     * @param nameTmp the name
     * @param position the position
     */
    public GameCharacter(final String nameTmp, final MapIndex position) {
        super(position);
        name = nameTmp;
        health = new HealthComponent();
        move = new IdleMoveComponent();
        inventory = new InventoryComponent();
        listeners = new ArrayList<>();
        eatDrink = new EatDrinkComponent();
    }

    /**
     * Acquire lock.
     * 
     * @return true, if successful
     */
    public boolean acquireLock() {
        boolean lockAcquired = false;
        if (!lock) {
            lock = true;
            lockAcquired = true;
            notifyListeners();
        }

        return lockAcquired;
    }

    /**
     * Add a listener to this character.
     * 
     * @param listener the listener to add
     */
    public void addListener(final ICharacterListener listener) {
        listeners.add(listener);
    }

    /**
     * Be idle movement.
     * 
     * @return the idle move component
     */
    public IdleMoveComponent beIdleMovement() {
        move = new IdleMoveComponent();
        return (IdleMoveComponent) move;
    }

    /**
     * Be still movement.
     * 
     * @return the still move component
     */
    public StillMoveComponent beStillMovement() {
        move = new StillMoveComponent();
        return (StillMoveComponent) move;
    }

    /**
     * Gets the eat drink.
     * 
     * @return the eat drink
     */
    public final EatDrinkComponent getEatDrink() {
        return eatDrink;
    }

    /**
     * Get the health component for this character.
     * 
     * @return the health component
     */
    public HealthComponent getHealth() {
        return health;
    }

    /**
     * Gets the inventory.
     * 
     * @return the inventory
     */
    public InventoryComponent getInventory() {
        return inventory;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if is dead.
     * 
     * @return true, if is dead
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Checks if is lock.
     * 
     * @return true, if is lock
     */
    public boolean isLock() {
        return lock;
    }

    /**
     * Kill.
     */
    public void kill() {
        Logger.getInstance().log(this, "Character died");
        beStillMovement();
        dead = true;
        notifyListeners();
    }

    /**
     * Release lock.
     */
    public void releaseLock() {
        lock = false;
        notifyListeners();
    }

    /**
     * Update.
     * 
     * @param player the player
     * @param region the region
     */
    public void update(final Player player, final Region region) {
        health.update(this, player, region);
        move.update(this, player, region);
        if (attack != null) {
            attack.update(this, player, region);
        }
        inventory.update(this, player, region);
        // TODO: animals and goblins should have a player or something
        if (player != null) {
            eatDrink.update(this, player, region);
        }
    }

    /**
     * Walk to position.
     * 
     * @param target the target
     * @param nextTo the next to
     * @return the walk move component
     */
    public WalkMoveComponent walkToPosition(final MapIndex target, final boolean nextTo) {
        move = new WalkMoveComponent(target, nextTo);
        return (WalkMoveComponent) move;
    }

    /**
     * Notify all the listeners.
     */
    private void notifyListeners() {
        for (ICharacterListener listener : listeners) {
            listener.charactedChanged();
        }
    }
}
