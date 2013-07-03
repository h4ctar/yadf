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

import logger.Logger;
import misc.MyRandom;
import simulation.character.DwarfManager;
import simulation.character.IDwarfManager;
import simulation.farm.FarmManager;
import simulation.farm.IFarmManager;
import simulation.item.IStockManager;
import simulation.item.Item;
import simulation.item.ItemTypeManager;
import simulation.item.StockManager;
import simulation.job.IJobManager;
import simulation.job.JobManager;
import simulation.map.MapIndex;
import simulation.military.IMilitaryManager;
import simulation.military.MilitaryManager;
import simulation.room.IRoomManager;
import simulation.room.RoomManager;
import simulation.workshop.IWorkshopManager;
import simulation.workshop.WorkshopManager;

/**
 * The player.
 */
public class Player extends AbstractGameObject implements IPlayer {

    /** The size of the embark area. */
    private static final int EMBARK_SIZE = 10;

    /** The name of the player. */
    private final String name;

    /** The job manager. */
    private final JobManager jobManager = new JobManager();

    /** The stock manager. */
    private final StockManager stockManager = new StockManager();

    /** The dwarf manager. */
    private final DwarfManager dwarfManager = new DwarfManager(this);

    /** The farm manager. */
    private final FarmManager farmManager = new FarmManager();

    /** The room manager. */
    private final RoomManager roomManager = new RoomManager();

    /** The workshop manager. */
    private final WorkshopManager workshopManager = new WorkshopManager();

    /** The military manager. */
    private final MilitaryManager militaryManager = new MilitaryManager();

    /** The region that this player is in. */
    private IRegion region;

    /**
     * Constructor.
     * @param playerName the players name
     */
    public Player(final String playerName) {
        name = playerName;
    }

    @Override
    public void setup(final IRegion regionTmp, final MapIndex embarkPosition, final int numberOfStartingDwarfs) {
        Logger.getInstance().log(this, "Setting up");
        region = regionTmp;
        jobManager.addDesignations(region, this);
        addEmbarkResources(embarkPosition);
        addEmbarkDwarfs(embarkPosition, numberOfStartingDwarfs);
    }

    /**
     * Gets the name of the player.
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    @Override
    public IDwarfManager getDwarfManager() {
        return dwarfManager;
    }

    @Override
    public IJobManager getJobManager() {
        return jobManager;
    }

    @Override
    public IStockManager getStockManager() {
        return stockManager;
    }

    @Override
    public IFarmManager getFarmManager() {
        return farmManager;
    }

    @Override
    public IRoomManager getRoomManager() {
        return roomManager;
    }

    @Override
    public IWorkshopManager getWorkshopManager() {
        return workshopManager;
    }

    @Override
    public IMilitaryManager getMilitaryManager() {
        return militaryManager;
    }

    @Override
    public void update() {
        dwarfManager.update();
        farmManager.update(this);
        workshopManager.update();
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
}
