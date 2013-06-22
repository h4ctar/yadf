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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import logger.Logger;
import simulation.AbstractEntity;
import simulation.IPlayer;
import simulation.Region;
import simulation.character.component.HealthComponent;
import simulation.character.component.ICharacterComponent;
import simulation.character.component.IHealthComponent;
import simulation.character.component.IMovementComponent;
import simulation.character.component.IdleMovementComponent;
import simulation.character.component.StillMovementComponent;
import simulation.map.MapIndex;

/**
 * The Class GameCharacter.
 */
public class GameCharacter extends AbstractEntity implements IGameCharacter {

    /** The serial version UID. */
    private static final long serialVersionUID = -6938089593424613235L;

    /** All the components. */
    private final Map<Class<? extends ICharacterComponent>, ICharacterComponent> components = new HashMap<>();

    /** The list of listeners. */
    private final List<ICharacterAvailableListener> availableListeners = new CopyOnWriteArrayList<>();

    private List<ICharacterListener> changeListeners;

    /** The dead. */
    protected boolean dead;

    /** The name. */
    protected String name;

    /** The lock. */
    private boolean locked;

    /** The player that this character belongs to. */
    private final IPlayer player;

    /**
     * Instantiates a new game character.
     * 
     * @param nameTmp the name
     * @param position the position
     * @param playerTmp the player that this dwarf belongs to
     */
    public GameCharacter(final String nameTmp, final MapIndex position, final IPlayer playerTmp) {
        super(position);
        name = nameTmp;
        player = playerTmp;
        setComponent(IHealthComponent.class, new HealthComponent(this));
        setComponent(IMovementComponent.class, new IdleMovementComponent(this));
    }

    /**
     * Acquire lock.
     * 
     * @return true, if successful
     */
    @Override
    public boolean acquireLock() {
        boolean lockAcquired = false;
        if (!locked) {
            locked = true;
            lockAcquired = true;
        }

        return lockAcquired;
    }

    @Override
    public void addListener(final ICharacterAvailableListener listener) {
        assert !availableListeners.contains(listener);
        availableListeners.add(listener);
    }

    @Override
    public void removeListener(final ICharacterAvailableListener listener) {
        assert availableListeners.contains(listener);
        availableListeners.remove(listener);
    }

    @Override
    public void addListener(final ICharacterListener listener) {
        assert !changeListeners.contains(listener);
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(final ICharacterListener listener) {
        assert changeListeners.contains(listener);
        changeListeners.remove(listener);
    }

    /**
     * Get a character component.
     * @param <T> the interface that the component extends
     * @param componentInterface the interface that the component extends
     * @return the component
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ICharacterComponent> T getComponent(final Class<T> componentInterface) {
        return (T) components.get(componentInterface);
    }

    /**
     * Set a character component.
     * @param <T> the interface that the component extends
     * @param componentInterface the interface that the component extends
     * @param component the component
     */
    @Override
    public <T extends ICharacterComponent> void setComponent(final Class<T> componentInterface, final T component) {
        components.put(componentInterface, component);
    }

    /**
     * Gets the name of the character.
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
    @Override
    public boolean isDead() {
        return dead;
    }

    /**
     * Checks if is lock.
     * 
     * @return true, if is lock
     */
    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * Kill.
     */
    @Override
    public void kill() {
        Logger.getInstance().log(this, "Character died");
        setComponent(IMovementComponent.class, new StillMovementComponent(this));
        dead = true;
    }

    /**
     * Release lock.
     */
    @Override
    public void releaseLock() {
        if (locked) {
            locked = false;
            if (!dead) {
                setComponent(IMovementComponent.class, new IdleMovementComponent(this));
                for (ICharacterAvailableListener listener : availableListeners) {
                    listener.characterAvailable(this);
                }
            }
        }
    }

    /**
     * Update all the components.
     * @param region the region
     */
    public void update(final Region region) {
        for (ICharacterComponent component : components.values()) {
            component.update(region);
        }
    }

    /**
     * Get the player that this dwarf belongs to.
     * @return the player that this dwarf belongs to
     */
    @Override
    public IPlayer getPlayer() {
        return player;
    }
}
