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

import java.util.List;

import simulation.Region;
import simulation.character.IGameCharacter;
import simulation.map.RegionMap;
import simulation.map.WalkableNode;

/**
 * The Class ChaseMoveComponent.
 */
public class ChaseMovementComponent extends AbstractMoveComponent implements IMovementComponent {

    /** The pursuee. */
    private final IGameCharacter pursuee;

    /** How many simulation steps since last walking step taken. */
    private int simulationSteps = 0;

    /** How many simulation steps between walking steps. */
    private final int walkSpeed = 10;

    /**
     * Instantiates a new chase move component.
     * @param character the character that this component belongs to
     * @param pursueeTmp the pursuee
     */
    public ChaseMovementComponent(final IGameCharacter character, final IGameCharacter pursueeTmp) {
        super(character);
        pursuee = pursueeTmp;
    }

    @Override
    public void kill() {
        // do nothing
    }

    @Override
    public void update(final Region region) {
        RegionMap map = region.getMap();

        fallDown(map);

        checkBlocked(map);

        simulationSteps++;
        if (simulationSteps > walkSpeed) {
            List<WalkableNode> adjacencies = map.getAdjacencies(getCharacter().getPosition());

            // Find adjacency that is closest to target dwarf
            WalkableNode bestNode = null;
            int bestDistance = Integer.MAX_VALUE;

            for (WalkableNode node : adjacencies) {
                if (region.getGameCharacter(node) != null) {
                    continue;
                }

                int distance = node.distance(pursuee.getPosition());

                if (bestNode == null) {
                    bestNode = node;
                    bestDistance = distance;
                } else {
                    if (distance < bestDistance) {
                        bestNode = node;
                        bestDistance = distance;
                    }
                }
            }
            if (bestNode != null) {
                getCharacter().setPosition(bestNode);
                simulationSteps = 0;
            }
        }
    }
}
