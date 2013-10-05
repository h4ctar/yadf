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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import yadf.logger.Logger;
import yadf.misc.MyRandom;
import yadf.misc.Noise;

/**
 * The Class RegionMap.
 */
public class RegionMap {

    /** The block types. */
    private BlockType[][][] blockTypes;

    /** The walkable nodes. */
    private List<WalkableNode> walkableNodes;

    /** The map size. */
    private MapIndex mapSize;

    /** The path planner. */
    private final PathPlanner pathPlanner = new PathPlanner();

    /** The listeners. */
    private final List<IMapListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Generates the map.
     * @param mapSizeTmp the size of the map
     */
    public void generateMap(final MapIndex mapSizeTmp) {
        mapSize = mapSizeTmp;
        Random random = MyRandom.getInstance();
        Noise.init();

        blockTypes = new BlockType[mapSize.x][mapSize.y][mapSize.z];

        for (int x = 0; x < mapSize.x; x++) {
            for (int y = 0; y < mapSize.y; y++) {
                for (int z = 0; z < mapSize.z; z++) {
                    int height = (int) ((Noise.noise((double) x / 100, (double) y / 100) + 8) / 10 * mapSize.z);
                    if (z < height) {
                        blockTypes[x][y][z] = BlockType.values()[random.nextInt(5) + 2];
                    } else if (z == height) {
                        blockTypes[x][y][z] = BlockType.GRASS;
                    } else {
                        blockTypes[x][y][z] = BlockType.EMPTY;
                    }
                }
            }
        }

        addRamps();

        setupWalkableNodes();
    }

    /**
     * Setup walkable nodes.
     */
    private void setupWalkableNodes() {
        walkableNodes = new ArrayList<>();

        // Create the walkable nodes
        for (int x = 0; x < mapSize.x; x++) {
            for (int y = 0; y < mapSize.y; y++) {
                int z = getHeight(x, y);
                walkableNodes.add(new WalkableNode(new MapIndex(x, y, z)));

                // If the block can be stood on, add a node above it (this is
                // for the ramps on hills)
                if (getBlock(x, y, z).isStandOn) {
                    walkableNodes.add(new WalkableNode(new MapIndex(x, y, z + 1)));
                }
            }
        }

        // Setup the adjacencies (Very brute force, but its only during loading)
        for (int i = 0; i < walkableNodes.size(); i++) {
            for (int j = i + 1; j < walkableNodes.size(); j++) {
                WalkableNode nodeI = walkableNodes.get(i);
                WalkableNode nodeJ = walkableNodes.get(j);

                MapIndex diff = nodeI.sub(nodeJ);
                if (Math.abs(diff.x) <= 1 && Math.abs(diff.y) <= 1) {
                    if (diff.z == 0 || // Its on the same level
                            (diff.z == 1 && getBlock(nodeJ).isClimb) || // Its
                                                                        // below
                            (diff.z == -1 && getBlock(nodeI).isClimb)) { // Its
                                                                         // above
                        nodeI.adjacencies.add(nodeJ);
                        nodeJ.adjacencies.add(nodeI);
                    }
                }
            }
        }
    }

    /**
     * Add a listener to the map.
     * @param listener the listener to add
     */
    public void addListener(final IMapListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener from the map.
     * @param listener the listener to remove
     */
    public void removeListener(final IMapListener listener) {
        listeners.remove(listener);
    }

    /**
     * Channel block.
     * @param index the index
     * @param blockType the block type
     */
    public void channelBlock(final MapIndex index, final BlockType blockType) {
        // It is already empty do nothing
        if (getBlock(index) == BlockType.EMPTY) {
            return;
        }

        if (blockType == null) {
            // Channel it - if it is on an edge, make it a ramp, else just make
            // it empty
            if (getBlock(index.add(0, 0, -1)).isSolid
                    && (!getBlock(index.add(-1, -1, 0)).isStandIn || !getBlock(index.add(0, -1, 0)).isStandIn
                            || !getBlock(index.add(1, -1, 0)).isStandIn || !getBlock(index.add(-1, 1, 0)).isStandIn
                            || !getBlock(index.add(0, 1, 0)).isStandIn || !getBlock(index.add(1, 1, 0)).isStandIn
                            || !getBlock(index.add(-1, 0, 0)).isStandIn || !getBlock(index.add(1, 0, 0)).isStandIn)) {
                setBlock(index, BlockType.RAMP);
            } else {
                setBlock(index, BlockType.EMPTY);
            }

            // Remove ramp above it
            if (getBlock(index.add(0, 0, 1)) == BlockType.RAMP) {
                setBlock(index.add(0, 0, 1), BlockType.EMPTY);
            }

            removeRampsAroundBlock(index);
        } else {
            setBlock(index, blockType);
        }
    }

    /**
     * Find path.
     * @param position the position
     * @param target the target
     * @return the list
     */
    public List<MapIndex> findPath(final MapIndex position, final MapIndex target) {
        WalkableNode positionNode = getWalkableNode(position);
        WalkableNode targetNode = getWalkableNode(target);

        if (positionNode == null) {
            Logger.getInstance().log(this, "Position node does not exist in walkable nodes");
            return null;
        }

        if (targetNode == null) {
            Logger.getInstance().log(this, "Target node does not exist in walkable nodes");
            return null;
        }

        return pathPlanner.findPath(positionNode, targetNode);
    }

    /**
     * This method should return the walkable nodes surrounding a particular block, even if that block is itself not
     * walkable.
     * @param position the position
     * @return the adjacencies
     */
    public List<WalkableNode> getAdjacencies(final MapIndex position) {
        WalkableNode node = getWalkableNode(position);
        if (node != null) {
            return node.adjacencies;
        }

        List<WalkableNode> adjacencies = new ArrayList<>();
        BlockType adjType;
        BlockType adjBelowType;
        BlockType type = blockTypes[position.x][position.y][position.z];
        MapIndex adjPosition;

        // Because iterating over all the walkable nodes is expensive, first
        // look for a block which is walkable, then
        for (int x = position.x - 1; x <= position.x + 1; x++) {
            for (int y = position.y - 1; y <= position.y + 1; y++) {
                // Add the adjacencies on the same level
                if (x != position.x || y != position.y) {
                    adjType = blockTypes[x][y][position.z];
                    adjBelowType = blockTypes[x][y][position.z - 1];
                    if (adjType.isStandIn && adjBelowType.isStandOn) {
                        adjPosition = new MapIndex(x, y, position.z);
                        adjacencies.add(getWalkableNode(adjPosition));
                    }
                }

                // Add the adjacency below
                adjType = blockTypes[x][y][position.z - 1];
                if (adjType.isClimb) {
                    adjPosition = new MapIndex(x, y, position.z - 1);
                    adjacencies.add(getWalkableNode(adjPosition));
                }

                // Add the adjacency above
                adjType = blockTypes[x][y][position.z + 1];
                if (adjType.isStandIn && type.isClimb) {
                    adjPosition = new MapIndex(x, y, position.z + 1);
                    adjacencies.add(getWalkableNode(adjPosition));
                }
            }
        }

        return adjacencies;
    }

    /**
     * Gets the block type at a passed location.
     * @param x x position
     * @param y y position
     * @param z z position
     * @return The block type
     */
    public BlockType getBlock(final int x, final int y, final int z) {
        if (x >= 0 && x < mapSize.x && y >= 0 && y < mapSize.y && z >= 0 && z < mapSize.z) {
            return blockTypes[x][y][z];
        }

        return BlockType.EMPTY;
    }

    /**
     * Gets the block type at a passed location.
     * @param blockIndex position of block to get type
     * @return The block type
     */
    public BlockType getBlock(final MapIndex blockIndex) {
        return getBlock(blockIndex.x, blockIndex.y, blockIndex.z);
    }

    /**
     * Gets the height at a position, this is going down from the sky until a solid block is reached (i.e. not a ramp)
     * @param x the x
     * @param y the y
     * @return the height
     */
    public int getHeight(final int x, final int y) {
        for (int z = mapSize.z - 1; z >= 0; z--) {
            if (getBlock(x, y, z).isStandIn && getBlock(x, y, z - 1).isStandOn && !getBlock(x, y, z - 1).isStandIn) {
                return z;
            }
        }

        return 0;
    }

    /**
     * Gets the map size.
     * @return the map size
     */
    public MapIndex getMapSize() {
        return mapSize;
    }

    /**
     * Gets the neighbour types.
     * @param b the b
     * @param neighbourTypes the neighbour types
     */
    public void getNeighbourTypes(final MapIndex b, final BlockType[] neighbourTypes) {
        int i = 0;
        neighbourTypes[i++] = getBlock(b.x - 1, b.y - 1, b.z);
        neighbourTypes[i++] = getBlock(b.x - 1, b.y + 1, b.z);
        neighbourTypes[i++] = getBlock(b.x - 1, b.y, b.z);
        neighbourTypes[i++] = getBlock(b.x + 1, b.y - 1, b.z);
        neighbourTypes[i++] = getBlock(b.x + 1, b.y + 1, b.z);
        neighbourTypes[i++] = getBlock(b.x + 1, b.y, b.z);
        neighbourTypes[i++] = getBlock(b.x, b.y - 1, b.z);
        neighbourTypes[i++] = getBlock(b.x, b.y + 1, b.z);
    }

    /**
     * Gets the neighbours.
     * @param b the b
     * @param neighbours the neighbours
     */
    public static void getNeighbours(final MapIndex b, final MapIndex[] neighbours) {
        int i = 0;
        neighbours[i++] = new MapIndex(b.x - 1, b.y - 1, b.z);
        neighbours[i++] = new MapIndex(b.x - 1, b.y + 1, b.z);
        neighbours[i++] = new MapIndex(b.x - 1, b.y, b.z);
        neighbours[i++] = new MapIndex(b.x + 1, b.y - 1, b.z);
        neighbours[i++] = new MapIndex(b.x + 1, b.y + 1, b.z);
        neighbours[i++] = new MapIndex(b.x + 1, b.y, b.z);
        neighbours[i++] = new MapIndex(b.x, b.y - 1, b.z);
        neighbours[i++] = new MapIndex(b.x, b.y + 1, b.z);
    }

    // TODO: implement quad tree
    /**
     * Gets the walkable node.
     * @param position the position
     * @return the walkable node
     */
    public WalkableNode getWalkableNode(final MapIndex position) {
        for (WalkableNode node : walkableNodes) {
            if (node.equals(position)) {
                return node;
            }
        }

        return null;
    }

    /**
     * Checks if is walkable.
     * @param mapIndex the map index
     * @return true, if is walkable
     */
    public boolean isWalkable(final MapIndex mapIndex) {
        return walkableNodes.contains(mapIndex);
    }

    /**
     * Mine block.
     * @param index the index
     */
    public void mineBlock(final MapIndex index) {
        BlockType blockType = blockTypes[index.x][index.y][index.z];
        setBlock(index, BlockType.valueOf("MINE_" + blockType));
        removeRampsAroundBlock(index);
    }

    /**
     * Sets the block.
     * @param index the index
     * @param type the type
     */
    public void setBlock(final MapIndex index, final BlockType type) {
        BlockType oldType = blockTypes[index.x][index.y][index.z];
        BlockType belowType = blockTypes[index.x][index.y][index.z - 1];
        BlockType aboveType = blockTypes[index.x][index.y][index.z + 1];

        blockTypes[index.x][index.y][index.z] = type;

        // Remove old walkable node
        if (belowType.isStandOn && oldType.isStandIn && !type.isStandIn) {
            WalkableNode node = getWalkableNode(index);
            if (node != null) {
                for (WalkableNode node2 : walkableNodes) {
                    node2.adjacencies.remove(node);
                }
                walkableNodes.remove(node);
            }
        }

        // Remove above walkable node
        if (!type.isStandOn && oldType.isStandOn && aboveType.isStandIn) {
            WalkableNode node = getWalkableNode(index.add(0, 0, 1));
            if (node != null) {
                for (WalkableNode node2 : walkableNodes) {
                    node2.adjacencies.remove(node);
                }
                walkableNodes.remove(node);
            }
        }

        // Create a new walkable node
        if (belowType.isStandOn && !oldType.isStandIn && type.isStandIn) {
            WalkableNode newNode = new WalkableNode(index);
            for (WalkableNode node : walkableNodes) {
                MapIndex diff = newNode.sub(node);
                if (Math.abs(diff.x) <= 1 && Math.abs(diff.y) <= 1) {
                    if (diff.z == 0 || // Its on the same level
                            (diff.z == 1 && getBlock(node).isClimb) || // Its
                                                                       // below
                            (diff.z == -1 && getBlock(newNode).isClimb)) { // Its
                                                                           // above
                        newNode.adjacencies.add(node);
                        node.adjacencies.add(newNode);
                    }
                }
            }
            walkableNodes.add(newNode);
        }

        // Create a new above walkable node
        if (!oldType.isStandOn && type.isStandOn && aboveType.isStandIn) {
            WalkableNode newNode = new WalkableNode(index.add(0, 0, 1));
            for (WalkableNode node : walkableNodes) {
                MapIndex diff = newNode.sub(node);
                if (Math.abs(diff.x) <= 1 && Math.abs(diff.y) <= 1) {
                    if (diff.z == 0 || // Its on the same level
                            (diff.z == 1 && getBlock(node).isClimb) || // Its
                                                                       // below
                            (diff.z == -1 && getBlock(newNode).isClimb)) { // Its
                                                                           // above
                        newNode.adjacencies.add(node);
                        node.adjacencies.add(newNode);
                    }
                }
            }
            walkableNodes.add(newNode);
        }

        notifyListeners(index);
    }

    /**
     * Adds ramps to the map, scans across the surface, finds the edges of hills and sets them as RAMP.
     */
    private void addRamps() {
        for (int x = 0; x < mapSize.x; x++) {
            for (int y = 0; y < mapSize.y; y++) {
                int h = getHeight(x, y);

                int h1 = getHeight(x - 1, y - 1);
                int h2 = getHeight(x, y - 1);
                int h3 = getHeight(x + 1, y - 1);
                int h4 = getHeight(x - 1, y);
                int h5 = getHeight(x + 1, y);
                int h6 = getHeight(x - 1, y + 1);
                int h7 = getHeight(x, y + 1);
                int h8 = getHeight(x + 1, y + 1);

                if (h1 > h || h2 > h || h3 > h || h4 > h || h5 > h || h6 > h || h7 > h || h8 > h) {
                    blockTypes[x][y][h] = BlockType.RAMP;
                }
            }
        }
    }

    /**
     * Notify all the listeners that the map has changed.
     * @param mapIndex the block that changed
     */
    private void notifyListeners(final MapIndex mapIndex) {
        for (IMapListener listener : listeners) {
            listener.mapChanged(mapIndex);
        }
    }

    /**
     * Read object.
     * @param in the in
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        generateMap(mapSize);
    }

    /**
     * Removes the ramps around block.
     * @param index the index
     */
    private void removeRampsAroundBlock(final MapIndex index) {
        // Remove ramps around it
        MapIndex[] neighbours = new MapIndex[8];
        getNeighbours(index, neighbours);

        for (MapIndex neighbour : neighbours) {
            boolean remove = true;

            // ignore this neighbour if it is not a ramp
            if (getBlock(neighbour) != BlockType.RAMP) {
                continue;
            }

            // get the types of the neighbours surrounding this neighbour
            BlockType[] neighbourTypes = new BlockType[8];
            getNeighbourTypes(neighbour, neighbourTypes);
            for (BlockType neighbourType : neighbourTypes) {
                if (neighbourType.isSolid) {
                    remove = false;
                    break;
                }
            }

            // if we got through all the neighbours of this neighbour and did
            // not find a solid block then remove this
            // ramp
            if (remove) {
                setBlock(neighbour, BlockType.EMPTY);
            }
        }
    }
}
