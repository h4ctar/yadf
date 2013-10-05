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

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.component.EatDrinkComponent;
import yadf.simulation.character.component.IEatDrinkComponent;
import yadf.simulation.character.component.IInventoryComponent;
import yadf.simulation.character.component.ISkillComponent;
import yadf.simulation.character.component.ISleepComponent;
import yadf.simulation.character.component.InventoryComponent;
import yadf.simulation.character.component.SkillComponent;
import yadf.simulation.character.component.SleepComponent;
import yadf.simulation.map.MapIndex;

/**
 * The Class Dwarf.
 */
public class Dwarf extends AbstractCharacter {

    /**
     * Instantiates a new dwarf.
     * @param name the name of the dwarf
     * @param position the position of the dwarf
     * @param region the region the dwarf is in
     * @param player the player that the dwarf belongs to
     */
    Dwarf(final String name, final MapIndex position, final IRegion region, final IPlayer player) {
        super(name, position, region, player);
        setComponent(ISkillComponent.class, new SkillComponent(this));
        setComponent(ISleepComponent.class, new SleepComponent(this));
        setComponent(IInventoryComponent.class, new InventoryComponent(this));
        setComponent(IEatDrinkComponent.class, new EatDrinkComponent(this));
    }
}
