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
package simulation.job;

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.item.Item;
import simulation.map.MapIndex;
import simulation.room.Room;

/**
 * The Class PlaceItemJob.
 */
public class PlaceItemJob extends AbstractJob {

    /**
     * The different states that this job can be in.
     */
    enum State {

        /** The start. */
        START,
        /** The haul item. */
        HAUL_ITEM
    }

    /** The current state of the job. */
    private State state = State.START;

    /** References to the haul job. */
    private HaulJob haulJob;

    /** The item. */
    private Item item;

    /** The item type name. */
    private final String itemTypeName;

    /** The position. */
    private final MapIndex position;

    /** The done. */
    private boolean done = false;

    /**
     * Instantiates a new place item job.
     * 
     * @param position the position
     * @param itemTypeName the item type name
     */
    public PlaceItemJob(final MapIndex position, final String itemTypeName) {
        this.itemTypeName = itemTypeName;
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        switch (state) {
        case HAUL_ITEM:
            return "Hauling item";
        case START:
            return "Waiting for item";
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message);

        if (haulJob != null) {
            haulJob.interrupt("Place item job has been interrupted");
        }

        if (item != null) {
            item.setUsed(false);
        }

        done = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Place " + itemTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case START:
            item = player.getStockManager().getUnusedItem(itemTypeName);

            if (item == null) {
                return;
            }

            item.setUsed(true);

            haulJob = new HaulJob(item, false, position);
            state = State.HAUL_ITEM;
            break;

        case HAUL_ITEM:
            haulJob.update(player, region);

            if (haulJob.isDone()) {
                if (!item.getPosition().equals(position)) {
                    interrupt("Item was not hauled");
                    return;
                }

                item.setUsed(false);
                item.setPlaced(true);

                Room room = player.getRoom(position);
                if (room != null) {
                    room.addItem(item);
                }

                done = true;
            }
            break;
        }
    }
}
