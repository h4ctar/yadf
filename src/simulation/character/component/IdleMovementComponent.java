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
import java.util.Random;

import misc.MyRandom;
import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.character.IMovementComponent;
import simulation.map.RegionMap;
import simulation.map.WalkableNode;

/**
 * The Class IdleMoveComponent.
 */
public class IdleMovementComponent extends AbstractMoveComponent implements IMovementComponent {

    /** How many simulation steps since last walking step taken. */
    private int simulationSteps = 0;

    /** Minimum number of simulation steps between walking steps. */
    private static final long MIN_WALK_DURATION = Region.SIMULATION_STEPS_PER_MINUTE * 8;

    /** Maximum number of simulation steps between walking steps. */
    private static final long MAX_WALK_DURATION = Region.SIMULATION_STEPS_PER_MINUTE * 16;

    @Override
    public void kill() {
        /* do nothing. */
    }

    @Override
    public void update(final GameCharacter character, final Player player, final Region region) {
        RegionMap map = region.getMap();
        Random random = MyRandom.getInstance();

        fallDown(character, map);
        checkBlocked(character, map);

        simulationSteps++;
        if (simulationSteps > MIN_WALK_DURATION + random.nextInt((int) (MAX_WALK_DURATION - MIN_WALK_DURATION))) {
            List<WalkableNode> adjacencies = map.getAdjacencies(character.getPosition());
            if (!adjacencies.isEmpty()) {
                WalkableNode node = adjacencies.get(random.nextInt(adjacencies.size()));

                if (region.getGameCharacter(node) == null) {
                    character.setPosition(node);
                    simulationSteps = 0;
                }
            }
        }
    }
}
