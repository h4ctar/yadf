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

import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.map.WalkableNode;

/**
 * The Class WalkMoveComponent.
 * 
 * @author Ben Smith (bensmith87@gmail.com)
 */
public class WalkMoveComponent extends AbstractMoveComponent {

    /** Where to go. */
    private final MapIndex target;

    /** If false will walk to the target, if true will try to get to any of positions surrounding squares. */
    private boolean nextTo = false;

    /** The path that the dwarf is walking along. */
    private transient List<MapIndex> path = null;

    /** How far along the path the dwarf is. */
    private int pathIndex = 0;

    /** Dwarf has arrived at target. */
    private boolean arrived = false;

    /** No path is available to the target. */
    private boolean noPath = false;

    /** How many simulation steps since last walking step taken. */
    private int simulationSteps = 0;

    /** How many simulation steps between walking steps. */
    private final long walkSpeed = Region.SIMULATION_STEPS_PER_MINUTE * 4;

    /**
     * Instantiates a new walk move component.
     * 
     * @param targetTmp the target
     * @param nextToTmp the next to
     */
    public WalkMoveComponent(final MapIndex targetTmp, final boolean nextToTmp) {
        target = new MapIndex(targetTmp);
        nextTo = nextToTmp;
    }

    /**
     * Checks if is arrived.
     * 
     * @return true, if is arrived
     */
    public boolean isArrived() {
        return arrived;
    }

    /**
     * Checks if is done.
     * 
     * @return true, if is done
     */
    public boolean isDone() {
        return arrived || noPath;
    }

    /**
     * Checks if is no path.
     * 
     * @return true, if is no path
     */
    public boolean isNoPath() {
        return noPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kill() {
        /* do nothing */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final GameCharacter character, final Player player, final Region region) {
        RegionMap map = region.getMap();

        fallDown(character, map);

        checkBlocked(character, map);

        if (checkArrived(character)) {
            return;
        }

        if (path == null) {
            if (!getPath(character, map)) {
                return;
            }
        }

        walkAlongPath(character, map);
    }

    /**
     * Check arrived.
     * 
     * @param character the character
     * @return true, if successful
     */
    private boolean checkArrived(final GameCharacter character) {
        MapIndex position = character.getPosition();

        if (nextTo) {
            for (int x = target.x - 1; x <= target.x + 1; x++) {
                for (int y = target.y - 1; y <= target.y + 1; y++) {
                    if (!(x == target.x && y == target.y) && position.equals(new MapIndex(x, y, target.z))) {
                        arrived = true;
                        return true;
                    }
                }
            }
        } else {
            if (position.equals(target)) {
                arrived = true;
                return true;
            }
        }

        if (pathIndex < 0) {
            return true;
        }

        return false;
    }

    /**
     * Gets the path.
     * 
     * @param character the character
     * @param map the map
     * @return the path
     */
    private boolean getPath(final GameCharacter character, final RegionMap map) {
        if (map.getWalkableNode(character.getPosition()) == null) {
            return false;
        }

        if (nextTo) {
            List<WalkableNode> adjacencies = map.getAdjacencies(target);

            for (MapIndex adjacency : adjacencies) {
                path = map.findPath(character.getPosition(), adjacency);
                if (path != null) {
                    break;
                }
            }
        } else {
            path = map.findPath(character.getPosition(), target);
        }

        // If the path is still null, there is no path available
        if (path == null) {
            noPath = true;
            return false;
        }

        pathIndex = path.size() - 1;

        return true;
    }

    /**
     * Walk along path.
     * 
     * @param character the character
     * @param map the map
     */
    private void walkAlongPath(final GameCharacter character, final RegionMap map) {
        simulationSteps++;
        if (simulationSteps > walkSpeed) {
            MapIndex nextPosition = path.get(pathIndex);

            // Is the next position walkable
            if (map.isWalkable(nextPosition)) {
                character.setPosition(nextPosition);
                pathIndex--;
            } else {
                path = null;
            }

            simulationSteps = 0;
        }
    }
}
