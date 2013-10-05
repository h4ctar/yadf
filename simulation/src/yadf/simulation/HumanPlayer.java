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
package yadf.simulation;

import java.util.List;
import java.util.Random;

import yadf.logger.Logger;
import yadf.misc.MyRandom;
import yadf.simulation.character.DwarfManager;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.farm.Farm;
import yadf.simulation.farm.FarmManager;
import yadf.simulation.farm.IFarmManager;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.item.ItemTypeManager;
import yadf.simulation.item.StockManager;
import yadf.simulation.item.Stockpile;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.JobManager;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.simulation.military.IMilitaryManager;
import yadf.simulation.military.MilitaryManager;
import yadf.simulation.room.IRoomManager;
import yadf.simulation.room.RoomManager;
import yadf.simulation.workshop.IWorkshop;
import yadf.simulation.workshop.IWorkshopManager;
import yadf.simulation.workshop.WorkshopManager;

/**
 * The human player.
 */
public class HumanPlayer extends AbstractPlayer {

    /** The size of the embark area. */
    private static final int EMBARK_SIZE = 10;

    /** The job manager. */
    private final JobManager jobManager = new JobManager();

    /** The stock manager. */
    private final StockManager stockManager = new StockManager();

    /** The dwarf manager. */
    private final DwarfManager dwarfManager = new DwarfManager(this);

    /** The farm manager. */
    private final FarmManager farmManager = new FarmManager(this);

    /** The room manager. */
    private final RoomManager roomManager = new RoomManager();

    /** The workshop manager. */
    private final WorkshopManager workshopManager = new WorkshopManager();

    /** The military manager. */
    private final MilitaryManager militaryManager = new MilitaryManager();

    /** The region that this player is currently in. */
    private IRegion region;

    /**
     * Constructor.
     * @param regionTmp the region that this player is in
     * @param playerName the players name
     * @param regionTmp
     */
    public HumanPlayer(final String playerName, final Region regionTmp) {
        super(playerName);
        region = regionTmp;
        setComponent(IJobManager.class, jobManager);
        setComponent(IStockManager.class, stockManager);
        setComponent(ICharacterManager.class, dwarfManager);
        setComponent(IFarmManager.class, farmManager);
        setComponent(IRoomManager.class, roomManager);
        setComponent(IWorkshopManager.class, workshopManager);
        setComponent(IMilitaryManager.class, militaryManager);
    }

    /**
     * Setup.
     * @param embarkPosition the embark position
     * @param numberOfStartingDwarfs the number of starting dwarfs
     */
    public void setup(final MapIndex embarkPosition, final int numberOfStartingDwarfs) {
        Logger.getInstance().log(this, "Setting up");
        jobManager.addDesignations(region, this);
        addEmbarkResources(embarkPosition);
        addEmbarkDwarfs(embarkPosition, numberOfStartingDwarfs);
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

    @Override
    public boolean checkAreaValid(final MapArea area) {
        // Check if overlap with stockpile
        for (Stockpile stockpile : getComponent(IStockManager.class).getStockpiles()) {
            if (area.operlapsArea(stockpile.getArea())) {
                return false;
            }
        }
        // Check that the area is free from workshops
        for (IWorkshop workshop : getComponent(IWorkshopManager.class).getWorkshops()) {
            if (area.operlapsArea(workshop.getArea())) {
                return false;
            }
        }
        // Check that the area is free from farms
        for (Farm farm : getComponent(IFarmManager.class).getFarms()) {
            if (area.operlapsArea(farm.getArea())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkAreaValid(final MapIndex mapIndex) {
        // Check if overlap with stockpile
        for (Stockpile stockpile : getComponent(IStockManager.class).getStockpiles()) {
            if (stockpile.getArea().containesIndex(mapIndex)) {
                return false;
            }
        }
        // Check that the area is free from workshops
        for (IWorkshop workshop : getComponent(IWorkshopManager.class).getWorkshops()) {
            if (workshop.getArea().containesIndex(mapIndex)) {
                return false;
            }
        }
        // Check that the area is free from farms
        for (Farm farm : getComponent(IFarmManager.class).getFarms()) {
            if (farm.getArea().containesIndex(mapIndex)) {
                return false;
            }
        }
        return true;
    }
}
