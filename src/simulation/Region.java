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
package simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import logger.Logger;
import misc.MyRandom;
import simulation.character.Animal;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.character.Goblin;
import simulation.item.Item;
import simulation.map.BlockType;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.stock.Stockpile;

/**
 * Region Contains all the data for a region, including the map, players and trees.
 */
public class Region implements Serializable {

    /** The number of starting animals. */
    private static final int NUMBER_OF_ANIMALS = 10;

    /** The probability that a tile will have a tree. */
    private static final double TREE_PROBABILITY = 0.1;

    /** The number of simulation steps in one hour. */
    public static final long SIMULATION_STEPS_PER_HOUR = 60;

    public static final long SIMULATION_STEPS_PER_MINUTE = SIMULATION_STEPS_PER_HOUR / 60;

    /** The number of simulation steps in one day. */
    public static final long SIMULATION_STEPS_PER_DAY = SIMULATION_STEPS_PER_HOUR * 24;

    /** The number of simulation steps in one week. */
    public static final long SIMULATION_STEPS_PER_WEEK = SIMULATION_STEPS_PER_DAY * 7;

    /** The number of simulation steps in one month. */
    public static final long SIMULATION_STEPS_PER_MONTH = (long) (SIMULATION_STEPS_PER_DAY * 30.4368);

    /** The number of simulation steps in one season. */
    public static final long SIMULATION_STEPS_PER_SEASON = (long) (SIMULATION_STEPS_PER_DAY * 91.3105);

    /** The number of simulation steps in one year. */
    public static final long SIMULATION_STEPS_PER_YEAR = (long) (SIMULATION_STEPS_PER_DAY * 365.242);

    /** The Map of this region. */
    private final RegionMap map = new RegionMap();

    /** A vector of the trees on the map. */
    private final List<Tree> trees = new CopyOnWriteArrayList<>();

    /** The animals. */
    private final List<Animal> animals = new ArrayList<>();

    /** The goblins. */
    private final List<Goblin> goblins = new ArrayList<>();

    /** A vector of all the players in this region. */
    private final List<Player> players = new ArrayList<>();

    /** The time. */
    private long time;

    /**
     * Adds the player.
     * 
     * @param player the player
     */
    public void addPlayer(final Player player) {
        Logger.getInstance().log(this, "Adding player " + player.getName());
        players.add(player);
    }

    /**
     * Adds the trees.
     */
    public void addTrees() {
        Random random = MyRandom.getInstance();
        for (int x = 0; x < map.getMapSize().x; x++) {
            for (int y = 0; y < map.getMapSize().y; y++) {
                int z = map.getHeight(x, y);

                if (map.getBlock(x, y, z + 1) == BlockType.RAMP) {
                    continue;
                }

                if (random.nextDouble() < TREE_PROBABILITY) {
                    trees.add(new Tree(new MapIndex(x, y, z)));
                }
            }
        }
    }

    /**
     * Checks if the location is valid for a new stockpile or building or similar thing that needs a flat area without
     * trees or items.
     * 
     * @param area The area that the new stockpile will occupy
     * @return True if the area is a valid location else false
     */
    public boolean checkAreaValid(final MapArea area) {
        // Check if overlap with stockpile
        for (Player player : players) {
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                MapArea stockpileArea = stockpile.getArea();
                if (area.pos.x < stockpileArea.pos.x + stockpileArea.width
                        && area.pos.x + area.width > stockpileArea.pos.x
                        && area.pos.y < stockpileArea.pos.y + stockpileArea.height
                        && area.pos.y + area.height > stockpileArea.pos.y && area.pos.z == stockpileArea.pos.z) {
                    return false;
                }
            }
        }

        // Check that area is free of trees
        for (Tree tree : trees) {
            if (tree.getPosition().x >= area.pos.x && tree.getPosition().x < area.pos.x + area.width
                    && tree.getPosition().y >= area.pos.y && tree.getPosition().y < area.pos.y + area.height
                    && tree.getPosition().z == area.pos.z) {
                return false;
            }
        }

        // Check that the area is free from other buildings
        // TODO: check if overlap with room

        // Check that area is flat
        for (int x = area.pos.x; x < area.pos.x + area.width; x++) {
            for (int y = area.pos.y; y < area.pos.y + area.height; y++) {
                if (!map.isWalkable(new MapIndex(x, y, area.pos.z))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check index valid.
     * 
     * @param mapIndex the map index
     * @return true, if successful
     */
    public boolean checkIndexValid(final MapIndex mapIndex) {
        // Check if overlap with stockpile
        for (Player player : players) {
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                MapArea area = stockpile.getArea();
                if (mapIndex.x < area.pos.x + area.width && mapIndex.x >= area.pos.x
                        && mapIndex.y < area.pos.y + area.height && mapIndex.y >= area.pos.y
                        && mapIndex.z == area.pos.z) {
                    return false;
                }
            }
        }

        // Check that area is free of trees
        for (Tree tree : trees) {
            if (tree.getPosition().x == mapIndex.x && tree.getPosition().y == mapIndex.y
                    && tree.getPosition().z == mapIndex.z) {
                return false;
            }
        }

        // Must be able to stand on the block below
        if (!map.isWalkable(mapIndex)) {
            return false;
        }

        // Can't build on a ramp or top of stair case
        if (map.getBlock(mapIndex.add(0, 0, -1)).isClimb) {
            return false;
        }

        // Check that the area is free from other buildings
        // TODO: check if overlap with room

        return true;
    }

    /**
     * Gets the animals.
     * 
     * @return the animals
     */
    public List<Animal> getAnimals() {
        return animals;
    }

    // TODO: make this actually return the closest dwarf, also move into player
    /**
     * Gets the closest dwarf.
     * 
     * @param position the position
     * @return the closest dwarf
     */
    public Dwarf getClosestDwarf(final MapIndex position) {
        int minDistance = Integer.MAX_VALUE;
        Dwarf minDwarf = null;

        for (Player player : players) {
            List<Dwarf> dwarfs = player.getDwarfs();
            for (Dwarf dwarf : dwarfs) {
                if (!dwarf.isDead()) {
                    int distance = dwarf.getPosition().distance(position);
                    if (minDwarf == null || distance < minDistance) {
                        minDistance = distance;
                        minDwarf = dwarf;
                    }
                }
            }
        }

        return minDwarf;
    }

    /**
     * Gets a reference to a dwarf at a particular location.
     * 
     * @param mouseIndex the mouse index
     * @return the dwarf
     */
    public GameCharacter getDwarf(final MapIndex mouseIndex) {
        for (Player player : players) {
            for (GameCharacter dwarf : player.getDwarfs()) {
                if (dwarf.getPosition().equals(mouseIndex)) {
                    return dwarf;
                }
            }
        }

        return null;
    }

    /**
     * Gets the game character.
     * 
     * @param position the position
     * @return the game character
     */
    public GameCharacter getGameCharacter(final MapIndex position) {
        for (Player player : players) {
            List<Dwarf> dwarfs = player.getDwarfs();
            for (Dwarf dwarf : dwarfs) {
                if (dwarf.getPosition().equals(position)) {
                    return dwarf;
                }
            }
        }

        for (GameCharacter goblin : goblins) {
            if (goblin.getPosition().equals(position)) {
                return goblin;
            }
        }

        return null;
    }

    /**
     * Gets the goblins.
     * 
     * @return the goblins
     */
    public List<Goblin> getGoblins() {
        return goblins;
    }

    /**
     * Gets a reference to an item at a particular location.
     * 
     * @param mouseIndex the mouse index
     * @return the item
     */
    public Item getItem(final MapIndex mouseIndex) {
        for (Player player : players) {
            for (Item item : player.getStockManager().getItems()) {
                if (item.getPosition().equals(mouseIndex)) {
                    return item;
                }
            }
        }

        return null;
    }

    /**
     * Gets the map.
     * 
     * @return the map
     */
    public RegionMap getMap() {
        return map;
    }

    /**
     * Gets the player.
     * 
     * @param playerId the player id
     * @return the player
     */
    public Player getPlayer(final int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }

        return null;
    }

    /**
     * Gets the players.
     * 
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Find if a position has a stockpile on it.
     * 
     * @param mapIndex The location you want to see if has a stockpile
     * @return A reference to a stockpile
     */
    public Stockpile getStockpile(final MapIndex mapIndex) {
        for (Player player : players) {
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                MapArea area = stockpile.getArea();
                if (mapIndex.x < area.pos.x + area.width && mapIndex.x >= area.pos.x
                        && mapIndex.y < area.pos.y + area.height && mapIndex.y >= area.pos.y
                        && mapIndex.z == area.pos.z) {
                    return stockpile;
                }
            }
        }

        return null;
    }

    /**
     * Gets the time string.
     * 
     * @return the time string
     */
    public String getTimeString() {
        long hour = time / SIMULATION_STEPS_PER_HOUR;
        long day = hour / 24;
        long year = (long) (day / 365.242);
        return "Year " + year + " Day " + (long) (day % 365.242) + " Hour " + hour % 24;
    }

    /**
     * Returns a tree at a specific location if it exists.
     * 
     * @param mapIndex The location you want to get a tree from
     * @return A reference to the tree at the location
     */
    public Tree getTree(final MapIndex mapIndex) {
        for (Tree tree : trees) {
            if (tree.getPosition().equals(mapIndex)) {
                return tree;
            }
        }

        return null;
    }

    /**
     * Gets the trees.
     * 
     * @return the trees
     */
    public List<Tree> getTrees() {
        return trees;
    }

    /**
     * Setup the region.
     * 
     * @param regionSize the size of the region
     */
    public void setup(final MapIndex regionSize) {
        Logger.getInstance().log(this, "Setting up");
        map.generateMap(regionSize);
        addTrees();
        addAnimals();
    }

    /**
     * Update.
     */
    public void update() {
        time++;

        // delete the trees that have been marked for deletion
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).getRemove()) {
                trees.remove(i);
                i--;
            }
        }

        for (Player player : players) {
            player.update(this);
        }

        for (GameCharacter animal : animals) {
            animal.update(null, this);
        }

        for (GameCharacter goblin : goblins) {
            goblin.update(null, this);
        }

        // spawnGoblins();
    }

    /**
     * Add animals to the region.
     */
    private void addAnimals() {
        Random random = MyRandom.getInstance();
        for (int i = 0; i < NUMBER_OF_ANIMALS; i++) {
            MapIndex position = new MapIndex();
            position.x = random.nextInt(map.getMapSize().x);
            position.y = random.nextInt(map.getMapSize().y);
            position.z = map.getHeight(position.x, position.y);

            animals.add(new Animal("Animal", position));
        }
    }

    /**
     * Spawn goblins.
     */
    private void spawnGoblins() {
        Random random = MyRandom.getInstance();
        if (time % 1000 == 0) {
            Logger.getInstance().log(this, "Goblin seige!");
            for (int i = 0; i < 4; i++) {
                MapIndex position = new MapIndex();

                position.x = random.nextInt(map.getMapSize().x);
                position.y = random.nextInt(map.getMapSize().y);
                position.z = map.getHeight(position.x, position.y);

                goblins.add(new Goblin("Goblin", position));
            }
        }
    }
}