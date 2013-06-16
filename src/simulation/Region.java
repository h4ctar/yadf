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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import logger.Logger;
import misc.MyRandom;
import simulation.character.Animal;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.character.Goblin;
import simulation.farm.Farm;
import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.map.BlockType;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.workshop.Workshop;

/**
 * Region Contains all the data for a region, including the map, players and trees.
 */
public class Region implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = -5907933435638776825L;

    /** The number of starting animals. */
    private static final int NUMBER_OF_ANIMALS = 10;

    /** The probability that a tile will have a tree. */
    private static final double TREE_PROBABILITY = 0.1;

    /** How many hours in one day. */
    private static final int HOURS_IN_A_DAY = 24;

    /** How many days in one year. */
    private static final double DAYS_IN_A_YEAR = 365.242;

    /** The number of simulation steps in one minute. */
    public static final long SIMULATION_STEPS_PER_MINUTE = 1;

    /** The number of simulation steps in one hour. */
    public static final long SIMULATION_STEPS_PER_HOUR = SIMULATION_STEPS_PER_MINUTE * 60;

    /** The number of simulation steps in one day. */
    public static final long SIMULATION_STEPS_PER_DAY = SIMULATION_STEPS_PER_HOUR * 24;

    /** The number of simulation steps in one week. */
    public static final long SIMULATION_STEPS_PER_WEEK = SIMULATION_STEPS_PER_DAY * 7;

    /** The number of simulation steps in one month. */
    public static final long SIMULATION_STEPS_PER_MONTH = (long) (SIMULATION_STEPS_PER_DAY * 30.4368);

    /** The number of simulation steps in one season. */
    public static final long SIMULATION_STEPS_PER_SEASON = (long) (SIMULATION_STEPS_PER_DAY * 91.3105);

    /** The number of simulation steps in one year. */
    public static final long SIMULATION_STEPS_PER_YEAR = (long) (SIMULATION_STEPS_PER_DAY * DAYS_IN_A_YEAR);

    /** The Map of this region. */
    private final RegionMap map = new RegionMap();

    /** A vector of the trees on the map. */
    private final Set<Tree> trees = new HashSet<>();

    /** The animals. */
    private final Set<Animal> animals = new HashSet<>();

    /** The goblins. */
    private final Set<Goblin> goblins = new HashSet<>();

    /** A vector of all the players in this region. */
    private final Set<Player> players = new HashSet<>();

    /** The time. */
    private long time;

    /**
     * Adds the player.
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
     * @param area The area that the new stockpile will occupy
     * @return True if the area is a valid location else false
     */
    public boolean checkAreaValid(final MapArea area) {
        for (Player player : players) {
            // Check if overlap with stockpile
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                if (area.operlapsArea(stockpile.getArea())) {
                    return false;
                }
            }
            // Check that the area is free from workshops
            for (Workshop workshop : player.getWorkshops()) {
                if (area.operlapsArea(workshop.getArea())) {
                    return false;
                }
            }
            // Check that the area is free from farms
            for (Farm farm : player.getFarms()) {
                if (area.operlapsArea(farm.getArea())) {
                    return false;
                }
            }
        }
        // Check that area is free of trees
        for (Tree tree : trees) {
            if (area.containesIndex(tree.getPosition())) {
                return false;
            }
        }
        for (int x = area.pos.x; x < area.pos.x + area.width; x++) {
            for (int y = area.pos.y; y < area.pos.y + area.height; y++) {
                // Check that area is flat
                if (!map.isWalkable(new MapIndex(x, y, area.pos.z))) {
                    return false;
                }
                // Can't build on a ramp or top of stair case
                if (map.getBlock(x, y, area.pos.z - 1).isClimb) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check index valid.
     * @param mapIndex the map index
     * @return true, if successful
     */
    public boolean checkIndexValid(final MapIndex mapIndex) {
        for (Player player : players) {
            // Check if overlap with stockpile
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                if (stockpile.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
            // Check that the area is free from workshops
            for (Workshop workshop : player.getWorkshops()) {
                if (workshop.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
            // Check that the area is free from farms
            for (Farm farm : player.getFarms()) {
                if (farm.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
        }
        // Check that area is free of trees
        for (Tree tree : trees) {
            if (tree.getPosition().equals(mapIndex)) {
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
        return true;
    }

    /**
     * Gets the animals.
     * @return the animals
     */
    public Set<Animal> getAnimals() {
        return animals;
    }

    /**
     * Gets the closest dwarf.
     * @param position the position
     * @return the closest dwarf
     */
    public Dwarf getClosestDwarf(final MapIndex position) {
        // TODO: make this actually return the closest dwarf, also move into player
        int minDistance = Integer.MAX_VALUE;
        Dwarf minDwarf = null;

        for (Player player : players) {
            Set<Dwarf> dwarfs = player.getDwarfManager().getDwarfs();
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
     * @param mouseIndex the mouse index
     * @return the dwarf
     */
    public GameCharacter getDwarf(final MapIndex mouseIndex) {
        for (Player player : players) {
            for (GameCharacter dwarf : player.getDwarfManager().getDwarfs()) {
                if (dwarf.getPosition().equals(mouseIndex)) {
                    return dwarf;
                }
            }
        }

        return null;
    }

    /**
     * Gets the game character.
     * @param position the position
     * @return the game character
     */
    public GameCharacter getGameCharacter(final MapIndex position) {
        for (Player player : players) {
            Set<Dwarf> dwarfs = player.getDwarfManager().getDwarfs();
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
     * @return the goblins
     */
    public Set<Goblin> getGoblins() {
        return goblins;
    }

    /**
     * Gets a reference to an item at a particular location.
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
     * @return the map
     */
    public RegionMap getMap() {
        return map;
    }

    /**
     * Gets the player with passed id.
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
     * Gets all the players.
     * @return the players
     */
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * Find if a position has a stockpile on it.
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
     * Gets a string representing the current time.
     * @return the time string
     */
    public String getTimeString() {
        long hour = time / SIMULATION_STEPS_PER_HOUR;
        long day = time / SIMULATION_STEPS_PER_DAY;
        long year = time / SIMULATION_STEPS_PER_YEAR;
        return "Year " + year + " Day " + (long) (day % DAYS_IN_A_YEAR) + " Hour " + hour % HOURS_IN_A_DAY;
    }

    /**
     * Returns a tree at a specific location if it exists.
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
     * Gets all the trees.
     * @return the trees
     */
    public Set<Tree> getTrees() {
        return trees;
    }

    /**
     * Setup the region.
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
        for (Player player : players) {
            player.update(this);
        }
        for (GameCharacter animal : animals) {
            animal.update(this);
        }
        for (GameCharacter goblin : goblins) {
            goblin.update(this);
        }
        for (Tree tree : trees.toArray(new Tree[0])) {
            if (tree.getRemove()) {
                trees.remove(tree);
            }
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
