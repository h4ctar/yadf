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

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import logger.Logger;
import misc.MyRandom;
import simulation.character.DwarfManager;
import simulation.character.IDwarfManager;
import simulation.farm.Farm;
import simulation.item.IStockManager;
import simulation.item.Item;
import simulation.item.ItemTypeManager;
import simulation.item.StockManager;
import simulation.job.IJobManager;
import simulation.job.JobManager;
import simulation.map.MapIndex;
import simulation.room.Room;
import simulation.workshop.Workshop;

/**
 * The player.
 */
public class Player extends AbstractGameObject implements IPlayer {

    /** The name of the player. */
    private String name;

    /** The job manager. */
    private final JobManager jobManager = new JobManager();

    /** The stock manager. */
    private final StockManager stockManager = new StockManager();

    /** The dwarf manager. */
    private final DwarfManager dwarfManager = new DwarfManager(this);

    /** The rooms. */
    private final Set<Room> rooms = new CopyOnWriteArraySet<>();

    /** The workshops. */
    private final Set<Workshop> workshops = new CopyOnWriteArraySet<>();

    /** The farms. */
    private final Set<Farm> farms = new CopyOnWriteArraySet<>();

    /** The region that this player is in. */
    private Region region;

    /** The size of the embark area. */
    private static final int EMBARK_SIZE = 10;

    /**
     * Setup.
     * @param playerName the players name
     * @param regionTmp the region that this player is in
     * @param embarkPosition the embark position
     * @param numberOfStartingDwarfs the number of starting dwarfs
     */
    public void setup(final String playerName, final Region regionTmp, final MapIndex embarkPosition,
            final int numberOfStartingDwarfs) {
        Logger.getInstance().log(this, "Setting up");
        name = playerName;
        region = regionTmp;
        jobManager.addDesignations(region, this);
        addEmbarkResources(embarkPosition);
        addEmbarkDwarfs(embarkPosition, numberOfStartingDwarfs);
    }

    /**
     * Adds a farm.
     * @param farm the farm
     */
    public void addFarm(final Farm farm) {
        farms.add(farm);
    }

    /**
     * Adds a room.
     * @param room the room
     */
    public void addRoom(final Room room) {
        // TODO: why was the stockmanager listening to the room
        // room.addListener(stockManager);
        rooms.add(room);
    }

    @Override
    public void removeRoom(final Room room) {
        // room.removeListener(stockManager);
        rooms.remove(room);
    }

    /**
     * Adds a workshop.
     * @param workshop the workshop
     */
    @Override
    public void addWorkshop(final Workshop workshop) {
        workshops.add(workshop);
    }

    @Override
    public void removeWorkshop(final Workshop workshop) {
        workshops.remove(workshop);
    }

    /**
     * Gets all the farms.
     * @return the farms
     */
    public Set<Farm> getFarms() {
        return farms;
    }

    /**
     * Gets the job manager.
     * @return the job manager
     */
    @Override
    public IJobManager getJobManager() {
        return jobManager;
    }

    /**
     * Gets the name of the player.
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the room.
     * @param roomId the room id
     * @return the room
     */
    public Room getRoom(final int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }

        return null;
    }

    /**
     * Gets the room.
     * @param index the index
     * @return the room
     */
    @Override
    public Room getRoom(final MapIndex index) {
        Room foundRoom = null;
        for (Room room : rooms) {
            if (room.getArea().containesIndex(index)) {
                foundRoom = room;
                break;
            }
        }
        return foundRoom;
    }

    /**
     * Gets the rooms.
     * @return the rooms
     */
    @Override
    public Set<Room> getRooms() {
        return rooms;
    }

    /**
     * Gets the stock manager.
     * @return the stock manager
     */
    @Override
    public IStockManager getStockManager() {
        return stockManager;
    }

    /**
     * Gets the workshop.
     * @param workshopId the workshop id
     * @return the workshop
     */
    public Workshop getWorkshop(final int workshopId) {
        for (Workshop workshop : workshops) {
            if (workshop.getId() == workshopId) {
                return workshop;
            }
        }

        return null;
    }

    /**
     * Get a workshop at a specific index.
     * @param index the index to look for a workshop
     * @return the workshop
     */
    public Workshop getWorkshop(final MapIndex index) {
        for (Workshop workshop : workshops) {
            if (workshop.hasIndex(index)) {
                return workshop;
            }
        }
        return null;
    }

    /**
     * Gets the workshops.
     * @return the workshops
     */
    public Set<Workshop> getWorkshops() {
        return workshops;
    }

    /**
     * Update.
     */
    public void update() {
        dwarfManager.update();
        // TODO: Don't update farms and workshops every step
        for (Farm farm : farms) {
            farm.update(this);
        }
        for (Workshop workshop : workshops) {
            workshop.update();
        }
    }

    /**
     * Add the starting dwarfs.
     * @param embarkPosition the position to embark
     * @param numberOfStartingDwarfs the number of starting dwarfs
     */
    private void addEmbarkDwarfs(final MapIndex embarkPosition, final int numberOfStartingDwarfs) {
        Random random = MyRandom.getInstance();
        for (int i = 0; i < numberOfStartingDwarfs; i++) {
            MapIndex pos = new MapIndex();
            pos.x = embarkPosition.x + random.nextInt(EMBARK_SIZE) - EMBARK_SIZE / 2;
            pos.y = embarkPosition.y + random.nextInt(EMBARK_SIZE) - EMBARK_SIZE / 2;
            pos.z = region.getMap().getHeight(pos.x, pos.y);
            dwarfManager.addNewDwarf(pos, region);
        }
    }

    /**
     * Add the starting resources.
     * @param embarkPosition the position to embark
     */
    private void addEmbarkResources(final MapIndex embarkPosition) {
        Random random = MyRandom.getInstance();
        List<Item> embarkItems = ItemTypeManager.getInstance().getEmbarkItems(this);
        for (Item item : embarkItems) {
            MapIndex position = new MapIndex(embarkPosition);
            position.x -= random.nextInt(EMBARK_SIZE) - EMBARK_SIZE / 2;
            position.y -= random.nextInt(EMBARK_SIZE) - EMBARK_SIZE / 2;
            position.z = region.getMap().getHeight(position.x, position.y);
            item.setPosition(position);
            stockManager.addItem(item);
        }
    }

    /**
     * Gets the dwarf manager.
     * @return the dwarf manager
     */
    @Override
    public IDwarfManager getDwarfManager() {
        return dwarfManager;
    }
}
