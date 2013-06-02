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

import simulation.Player;
import simulation.Region;
import simulation.character.component.SkillComponent;
import simulation.character.component.WorkComponent;
import simulation.map.MapIndex;

/**
 * The Class Dwarf.
 */
public class Dwarf extends GameCharacter {

    /** The serial version UID. */
    private static final long serialVersionUID = 5472142489726028220L;

    /** The skill. */
    private final SkillComponent skill;

    /** The work. */
    private final WorkComponent work;

    /**
     * Instantiates a new dwarf.
     * 
     * @param name the name
     * @param position the position
     */
    public Dwarf(final String name, final MapIndex position) {
        super(name, position);

        skill = new SkillComponent();
        work = new WorkComponent();
    }

    /**
     * Gets the skill.
     * 
     * @return the skill
     */
    public SkillComponent getSkill() {
        return skill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Player player, final Region region) {
        skill.update(this, player, region);
        work.update(this, player, region);

        super.update(player, region);
    }
}
