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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import logger.Logger;
import simulation.character.Goblin;
import simulation.character.IGameCharacter;
import simulation.farm.Farm;
import simulation.item.Stockpile;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.tree.ITreeManager;
import simulation.tree.TreeManager;
import simulation.workshop.Workshop;

/**
 * Region Contains all the data for a region, including the map, players and trees.
 */
public class Region implements IRegion {

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

    /** The tree manager for this region. */
    private final TreeManager treeManager = new TreeManager(this);

    /** The goblins. */
    // TODO: move into a player
    private final Set<Goblin> goblins = new CopyOnWriteArraySet<>();

    /** A vector of all the players in this region. */
    private final Set<Player> players = new LinkedHashSet<>();

    /** The time. */
    private long time;

    /** Listeners to be notified at a certain time. */
    private final Map<Long, Set<ITimeListener>> timeListeners = new HashMap<>();

    /**
     * Setup the region.
     * @param regionSize the size of the region
     */
    public void setup(final MapIndex regionSize) {
        Logger.getInstance().log(this, "Setting up");
        map.generateMap(regionSize);
        treeManager.addTrees();
    }

    /**
     * Adds the player.
     * @param player the player
     */
    public void addPlayer(final Player player) {
        Logger.getInstance().log(this, "Adding player " + player.getName());
        players.add(player);
    }

    // TODO: both these check methods need to be refactored to off load the work to the proper responsibles
    @Override
    public boolean checkAreaValid(final MapArea area) {
        for (Player player : players) {
            // Check if overlap with stockpile
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                if (area.operlapsArea(stockpile.getArea())) {
                    return false;
                }
            }
            // Check that the area is free from workshops
            for (Workshop workshop : player.getWorkshopManager().getWorkshops()) {
                if (area.operlapsArea(workshop.getArea())) {
                    return false;
                }
            }
            // Check that the area is free from farms
            for (Farm farm : player.getFarmManager().getFarms()) {
                if (area.operlapsArea(farm.getArea())) {
                    return false;
                }
            }
        }
        if (!treeManager.getTrees(area).isEmpty()) {
            return false;
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

    @Override
    public boolean checkIndexValid(final MapIndex mapIndex) {
        for (Player player : players) {
            // Check if overlap with stockpile
            for (Stockpile stockpile : player.getStockManager().getStockpiles()) {
                if (stockpile.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
            // Check that the area is free from workshops
            for (Workshop workshop : player.getWorkshopManager().getWorkshops()) {
                if (workshop.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
            // Check that the area is free from farms
            for (Farm farm : player.getFarmManager().getFarms()) {
                if (farm.getArea().containesIndex(mapIndex)) {
                    return false;
                }
            }
        }
        if (treeManager.getTree(mapIndex) != null) {
            return false;
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
     * Gets the goblins.
     * @return the goblins
     */
    public Set<Goblin> getGoblins() {
        return goblins;
    }

    @Override
    public ITreeManager getTreeManager() {
        return treeManager;
    }

    @Override
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
     * Update.
     */
    public void update() {
        time++;
        if (timeListeners.containsKey(time)) {
            for (ITimeListener listener : timeListeners.remove(time)) {
                listener.notifyTimeEvent();
            }
        }
        for (Player player : players) {
            player.update();
        }
        for (IGameCharacter goblin : goblins) {
            goblin.update();
        }
    }

    @Override
    public long addTimeListener(final long duration, final ITimeListener listener) {
        long notifyTime = time + duration;
        if (!timeListeners.containsKey(notifyTime)) {
            timeListeners.put(notifyTime, new LinkedHashSet<ITimeListener>());
        }
        timeListeners.get(notifyTime).add(listener);
        return notifyTime;
    }

    @Override
    public void removeTimeListener(final long notifyTime, final ITimeListener listener) {
        if (timeListeners.containsKey(notifyTime)) {
            timeListeners.get(notifyTime).remove(listener);
        }
    }
}
