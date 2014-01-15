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
package yadf.simulation.item;

import java.util.LinkedHashSet;
import java.util.Set;

import yadf.simulation.IEntity;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectAvailableListener;
import yadf.simulation.IPlayer;
import yadf.simulation.job.HaulJob;
import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobListener;
import yadf.simulation.job.IJobManager;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * The Class Stockpile.
 */
public class Stockpile extends ItemManager implements IItemManager, IEntity, IJobListener, IGameObjectAvailableListener {

    /** Is the position in the stockpile used. */
    private final boolean[][] used;

    /** What item type the stockpile accepts. */
    private final Set<ItemType> itemTypes = new LinkedHashSet<>();

    private MapArea area;

    /** The player that this stockpile belongs to. */
    private final IPlayer player;

    /**
     * An array of haul tasks that have been created for this stockpile, its remembered so they can be canceled if the
     * stockpile is deleted or its items to collect changes.
     */
    private final Set<HaulJob> haulJobs = new LinkedHashSet<>();

    /**
     * Constructor for the stockpile.
     * @param area the area the stockpile will occupy
     * @param playerTmp the player the stockpile belongs to
     */
    public Stockpile(MapArea areaTmp, IPlayer playerTmp) {
        area = areaTmp;
        player = playerTmp;
        used = new boolean[area.width][area.height];

        for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
            addItemType(itemType);
        }
        
        player.getComponent(IStockManager.class).getUnstoredItemManager().addAvailableListener(this);
    }

    public void addItemType(final ItemType itemType) {
        assert !itemTypes.contains(itemType);
        itemTypes.add(itemType);
        createHaulJobs();
    }

    public void removeItemType(final ItemType itemType) {
        assert itemTypes.contains(itemType);
        itemTypes.remove(itemType);
        cancelHaulJobs();
    }

    /**
     * Create haul tasks to fill up the stockpile.
     */
    private void createHaulJobs() {
        if (!itemTypes.isEmpty()) {
            for (int x = 0; x < area.width; x++) {
                for (int y = 0; y < area.height; y++) {
                    if (!used[x][y]) {
                        Item item = player.getComponent(IStockManager.class).getUnstoredItemManager().getItem(itemTypes);
                        if (item != null) {
                            item.setAvailable(false);
                            used[x][y] = true;
                            HaulJob haulJob = new HaulJob(item, this, getPosition().add(x, y, 0), player);
                            haulJob.addListener(this);
                            player.getComponent(IJobManager.class).addJob(haulJob);
                            haulJobs.add(haulJob);
                        }
                    }
                }
            }
        }
    }
    
    private void cancelHaulJobs() {
        for(HaulJob job : haulJobs) {
            if(!itemTypes.contains(job.getItem().itemType)) {
                MapIndex pos = job.getPosition().sub(getPosition());
                assert used[pos.x][pos.y];
                used[pos.x][pos.y] = false;
                job.interrupt("Stockpile no longer accepts this item type");
            }
        }
    }

    @Override
    public void jobDone(IJob job) {
        assert job.isDone();
        assert haulJobs.contains(job);
        haulJobs.remove(job);
        ((HaulJob)job).getItem().setAvailable(true);
    }

    @Override
    public void jobChanged(IJob job) {
        // Do nothing
    }
    
    @Override
    public void removeGameObject(Item item) {
        super.removeGameObject(item);
        MapIndex pos = item.getPosition().sub(getPosition());
        assert used[pos.x][pos.y];
        used[pos.x][pos.y] = false;
        createHaulJobs();
    }
    
    @Override
    public void gameObjectAvailable(IGameObject gameObject) {
        createHaulJobs();
    }

    @Override
    public String toString() {
        return "Stockpile";
    }

    @Override
    public MapIndex getPosition() {
        return area.pos;
    }

    @Override
    public void setPosition(MapIndex positionTmp) {
        assert false;
    }

    @Override
    public MapArea getArea() {
        return area;
    }

    @Override
    public boolean containsIndex(MapIndex index) {
        return area.containesIndex(index);
    }
}
