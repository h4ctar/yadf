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
package yadf.simulation.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yadf.logger.Logger;

/**
 * The Class PathPlanner.
 */
class PathPlanner {

    /** How much it costs to walk straight north, south, east, west or up or down. */
    private static final int COST_OF_SQUARE_MOVEMENT = 10;

    /** How much it consts to walk diagonally. */
    private static final int COST_OF_DIAGONAL_MOVEMENT = (int) (COST_OF_SQUARE_MOVEMENT * Math.sqrt(2));

    /**
     * Plan a path.
     * @param start the start
     * @param goal the goal
     * @return the list
     */
    public List<MapIndex> findPath(final WalkableNode start, final MapIndex goal) {
        List<MapIndex> path = new ArrayList<>();
        SortedList openList = new SortedList();
        List<WalkableNode> closedList = new ArrayList<>();
        WalkableNode current;

        start.g = 0;
        start.h = (Math.abs(goal.x - start.x) + Math.abs(goal.y - start.y) + Math.abs(goal.z - start.z))
                * COST_OF_SQUARE_MOVEMENT;
        start.f = start.g + start.h;

        // 1) Add the starting square (or node) to the open list.
        openList.add(start);

        // 2) Repeat the following:
        while (true) {
            // a) Look for the lowest F cost square on the open list. We refer to this as the current square.
            current = openList.first();

            // b) Switch it to the closed list.
            openList.remove(current);
            closedList.add(current);

            // c) For each of the 8 squares adjacent to this current square...
            for (WalkableNode adjacent : current.adjacencies) {
                // If it is not walkable or if it is on the closed list, ignore it. Otherwise do the following.
                if (closedList.contains(adjacent)) {
                    continue;
                }

                if (!openList.contains(adjacent)) {
                    // If it isn’t on the open list, add it to the open list. Make the current square the parent of this
                    // square. Record the F, G, and H costs of the square.
                    openList.add(adjacent);

                    adjacent.parent = current;

                    if (adjacent.x == current.x || adjacent.y == current.y) {
                        adjacent.g = current.g + COST_OF_SQUARE_MOVEMENT;
                    } else {
                        adjacent.g = current.g + COST_OF_DIAGONAL_MOVEMENT;
                    }
                    adjacent.h = (Math.abs(goal.x - adjacent.x) + Math.abs(goal.y - adjacent.y) + Math.abs(goal.z
                            - adjacent.z))
                            * COST_OF_SQUARE_MOVEMENT;
                    adjacent.f = adjacent.g + adjacent.h;
                } else {
                    // If it is on the open list already, check to see if this path to that square is better, using G
                    // cost as the measure. A lower G cost means that this is a better path. If so, change the parent of
                    // the square to the current square, and recalculate the G and F scores of the square. If you are
                    // keeping your open list sorted by F score, you may need to resort the list to account for the
                    // change.
                    int newG;

                    if (adjacent.x == current.x || adjacent.y == current.y) {
                        newG = current.g + COST_OF_SQUARE_MOVEMENT;
                    } else {
                        newG = current.g + COST_OF_DIAGONAL_MOVEMENT;
                    }

                    if (newG < adjacent.g) {
                        adjacent.parent = current;
                        adjacent.g = newG;
                        adjacent.f = adjacent.g + adjacent.h;
                        openList.sort();
                    }
                }
            }

            // d) Stop when you:
            // Add the target square to the closed list, in which case the path has been found (see note below)
            if (current.equals(goal)) {
                break;
            }

            // Fail to find the target square, and the open list is empty. In this case, there is no path.
            if (openList.size() == 0) {
                Logger.getInstance().log(this, "Open list is empty (No possible path)");
                return null;
            }
        }

        // 3) Save the path. Working backwards from the target square, go from each square to its parent square until
        // you reach the starting square. That is your path.
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = current.parent;
        }

        return path;
    }

    /**
     * A simple sorted list.
     * 
     * kevin
     * http://code.google.com/p/dronedefense/source/browse/DroidDefense/src/com/stropheware/pathfinding/Node.java?r=46
     */
    private class SortedList {

        /** The list of elements. */
        private final List<WalkableNode> list = new ArrayList<>();

        /**
         * Add an element to the list - causes sorting.
         * @param o The element to add
         */
        public void add(final WalkableNode o) {
            list.add(o);
            Collections.sort(list);
        }

        /**
         * Check if an element is in the list.
         * @param o The element to search for
         * @return True if the element is in the list
         */
        public boolean contains(final WalkableNode o) {
            return list.contains(o);
        }

        /**
         * Retrieve the first element from the list.
         * @return The first element from the list
         */
        public WalkableNode first() {
            return list.get(0);
        }

        /**
         * Remove an element from the list.
         * @param o The element to remove
         */
        public void remove(final WalkableNode o) {
            list.remove(o);
        }

        /**
         * Get the number of elements in the list.
         * @return The number of element in the list
         */
        public int size() {
            return list.size();
        }

        /**
         * Sort.
         */
        public void sort() {
            Collections.sort(list);
        }
    }
}
