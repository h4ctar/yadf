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
package yadf.simulation.character.component;

import java.util.List;

import yadf.simulation.character.IGameCharacter;
import yadf.simulation.map.MapIndex;
import yadf.simulation.map.RegionMap;
import yadf.simulation.map.WalkableNode;

/**
 * The Class MoveComponent.
 */
abstract class AbstractMoveComponent extends AbstractCharacterComponent {

    /**
     * Constructor.
     * @param characterTmp the character that this component belongs to
     */
    AbstractMoveComponent(final IGameCharacter characterTmp) {
        super(characterTmp);
    }

    /**
     * Check blocked.
     * @param map the map
     */
    protected void checkBlocked(final RegionMap map) {
        MapIndex position = getCharacter().getPosition();

        // Move if can no longer stand here (wall has been build)
        if (!map.getBlock(position).isStandIn) {
            List<WalkableNode> adjacencies = map.getAdjacencies(position);
            if (!adjacencies.isEmpty()) {
                getCharacter().setPosition(new MapIndex(adjacencies.get(0)));
            }
        }
    }

    /**
     * Fall down.
     * @param map the map
     */
    protected void fallDown(final RegionMap map) {
        MapIndex position = getCharacter().getPosition();

        // Fall down if the block below the dwarf can't be stood on
        if (!map.getBlock(position.add(0, 0, -1)).isStandOn) {
            position.z--;
        }

        getCharacter().setPosition(position);
    }
}
