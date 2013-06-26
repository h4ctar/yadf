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

    /** The list of listeners that want to be notified when the dwarf becomes available. */
    private final List<ICharacterAvailableListener> availableListeners = new CopyOnWriteArrayList<>();

    /** The listeners that want to be notified when anything changes. */
    private final List<ICharacterListener> changeListeners = new CopyOnWriteArrayList<>();

    /** The dead. */
    protected boolean dead;

    /** The name. */
    protected String name;

    /** The lock. */
    private boolean locked;

    /** The player that this character belongs to. */
    private final IPlayer player;

    /** The region that the character is currently in. */
    private final Region region;

    /**
     * Instantiates a new game character.
     * @param nameTmp the name
     * @param position the position
     * @param regionTmp the region that the character is currently in
     * @param playerTmp the player that this dwarf belongs to
     */
    public GameCharacter(final String nameTmp, final MapIndex position, final Region regionTmp,
            final IPlayer playerTmp) {
        super(position);
        name = nameTmp;
        region = regionTmp;
        player = playerTmp;
        setComponent(IHealthComponent.class, new HealthComponent(this));
        setComponent(IMovementComponent.class, new IdleMovementComponent(this));
    }

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
        availableListeners.add(listener);
    }

    @Override
    public void removeListener(final ICharacterAvailableListener listener) {
        availableListeners.remove(listener);
    }

    @Override
    public void addListener(final ICharacterListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(final ICharacterListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ICharacterComponent> T getComponent(final Class<T> componentInterface) {
        return (T) components.get(componentInterface);
    }

    @Override
    public <T extends ICharacterComponent> void setComponent(final Class<T> componentInterface, final T component) {
        Logger.getInstance().log(
                this,
                "Set component: " + componentInterface.getSimpleName() + " = "
                        + component.getClass().getSimpleName());
        components.put(componentInterface, component);
    }

    /**
     * Gets the name of the character.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void kill() {
        Logger.getInstance().log(this, "Character died");
        setComponent(IMovementComponent.class, new StillMovementComponent(this));
        dead = true;
    }

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

    @Override
    public void update() {
        for (ICharacterComponent component : components.values()) {
            component.update(region);
        }
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public Region getRegion() {
        return region;
    }
}
