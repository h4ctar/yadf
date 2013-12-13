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
package yadf.simulation.character;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import yadf.logger.Logger;
import yadf.simulation.AbstractEntity;
import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.component.HealthComponent;
import yadf.simulation.character.component.ICharacterComponent;
import yadf.simulation.character.component.IHealthComponent;
import yadf.simulation.character.component.IMovementComponent;
import yadf.simulation.character.component.IdleMovementComponent;
import yadf.simulation.character.component.StillMovementComponent;
import yadf.simulation.job.IJob;
import yadf.simulation.map.MapIndex;

/**
 * An abstract character to implement boring stuff.
 */
class AbstractCharacter extends AbstractEntity implements IGameCharacter {

    /** All the components. */
    private final Map<Class<? extends ICharacterComponent>, ICharacterComponent> components = new ConcurrentHashMap<>();

    /** The dead. */
    private boolean dead;

    /** The name. */
    private String name;

    /** The dwarfs current job. */
    private IJob job = null;

    /** The player that this character belongs to. */
    private final IPlayer player;

    /** The region that the character is currently in. */
    private final IRegion region;

    /**
     * Instantiates a new game character.
     * @param nameTmp the name
     * @param position the position
     * @param regionTmp the region that the character is currently in
     * @param playerTmp the player that this dwarf belongs to
     */
    public AbstractCharacter(final String nameTmp, final MapIndex position, final IRegion regionTmp,
            final IPlayer playerTmp) {
        super(position);
        Logger.getInstance().log(this, "Character created");
        name = nameTmp;
        region = regionTmp;
        player = playerTmp;
        setComponent(IHealthComponent.class, new HealthComponent(this));
        setComponent(IMovementComponent.class, new IdleMovementComponent(this));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setJob(final IJob jobTmp) {
        assert job == null;
        job = jobTmp;
        super.setAvailable(false);
    }

    @Override
    public void setAvailable(final boolean available) {
        assert available;
        assert job != null;
        job = null;
        if (!dead) {
            setComponent(IMovementComponent.class, new IdleMovementComponent(this));
        }
        super.setAvailable(available);
    }

    @Override
    public void kill() {
        Logger.getInstance().log(this, "Character died");
        if (!dead) {
            if (job != null) {
                job.interrupt("Character died");
            }
            assert job == null;
            setComponent(IMovementComponent.class, new StillMovementComponent(this));
            for (ICharacterComponent component : components.values()) {
                component.kill();
            }
            dead = true;
            notifyGameObjectChanged();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ICharacterComponent> T getComponent(final Class<T> componentInterface) {
        return (T) components.get(componentInterface);
    }

    @Override
    public <T extends ICharacterComponent> void setComponent(final Class<T> componentInterface, final T component) {
        Logger.getInstance()
                .log(this,
                        "Set component: " + componentInterface.getSimpleName() + " = "
                                + component.getClass().getSimpleName());
        components.put(componentInterface, component);
    }

    @Override
    public <T extends ICharacterComponent> void removeComponent(final Class<T> componentInterface) {
        components.remove(componentInterface);
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
    public void update() {
        if (!dead) {
            for (ICharacterComponent component : components.values()) {
                component.update(region);
            }
        }
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public IRegion getRegion() {
        return region;
    }
}
