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

import simulation.IPlayer;
import simulation.item.Item;
import simulation.job.jobstate.HaulItemState;
import simulation.job.jobstate.IJobState;
import simulation.map.MapIndex;
import simulation.room.Room;

/**
 * The Class PlaceItemJob.
 */
public class PlaceItemJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -7795181197544909062L;

    /** The item to place. */
    private Item item;

    /** The item type name. */
    private final String itemTypeName;

    /** The position to place the item. */
    private final MapIndex position;

    /**
     * Instantiates a new place item job.
     * @param positionTmp the position
     * @param itemTypeNameTmp the item type name
     * @param player the player that this job belongs to
     */
    public PlaceItemJob(final MapIndex positionTmp, final String itemTypeNameTmp, final IPlayer player) {
        super(player);
        position = positionTmp;
        itemTypeName = itemTypeNameTmp;
        setJobState(new PlaceItemState());
    }

    @Override
    public String toString() {
        return "Place " + itemTypeName;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    /**
     * The place item job state.
     */
    private class PlaceItemState extends HaulItemState {

        /**
         * Constructor.
         */
        public PlaceItemState() {
            super(item, position, PlaceItemJob.this);
        }

        @Override
        public void transitionOutOf() {
            item.setUsed(false);
            item.setPlaced(true);
            Room room = getPlayer().getRoom(position);
            if (room != null) {
                room.addItem(item);
            }
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
