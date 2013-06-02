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
package simulation.job.designation;

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.component.WalkMoveComponent;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.WasteTimeJob;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;
import simulation.map.RegionMap;

/**
 * Task to do some mining.
 */
public class MineJob extends AbstractDesignationJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -43625505095983333L;

    /**
     * The different states that this job can be in.
     */
    enum State {
        /** The start. */
        START,
        /** The goto. */
        GOTO,
        /** The mine. */
        MINE
    }

    /** The state. */
    private State state = State.START;

    /** The index of the block to be mined. */
    private final MapIndex position;

    /** The designation. */
    private final MineDesignation designation;

    /** The move component. */
    private WalkMoveComponent moveComponent = null;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** Amount of time to spend mining (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The dwarf. */
    private Dwarf dwarf = null;

    /** The done. */
    private boolean done = false;

    /**
     * Constructor for the mine task.
     * @param position the position
     * @param designation the designation
     */
    public MineJob(final MapIndex position, final MineDesignation designation) {
        this.position = position;
        this.designation = designation;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public String getStatus() {
        return "Mining";
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        designation.removeFromDesignation(position);

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Channel job was interrupted");
        }

        if (dwarf != null) {
            dwarf.beIdleMovement();
            dwarf.releaseLock();
        }

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Mine";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case START:
            if (region.getMap().getAdjacencies(position).isEmpty()) {
                return;
            }

            dwarf = player.getIdleDwarf(LaborTypeManager.getInstance().getLaborType("Mining"));

            if (dwarf == null) {
                return;
            }

            moveComponent = dwarf.walkToPosition(position, true);

            state = State.GOTO;
            break;

        case GOTO:
            if (moveComponent.isNoPath()) {
                interrupt("No path to site");
                return;
            }

            if (moveComponent.isArrived()) {
                state = State.MINE;
                wasteTimeJob = new WasteTimeJob(dwarf, DURATION);
            }
            break;

        case MINE:
            wasteTimeJob.update(player, region);

            if (!wasteTimeJob.isDone()) {
                return;
            }

            RegionMap map = region.getMap();

            String itemTypeName = map.getBlock(position).itemMined;
            if (itemTypeName != null) {
                ItemType itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
                Item blockItem = ItemTypeManager.getInstance().createItem(position, itemType, player);
                player.getStockManager().addItem(blockItem);
            }
            map.mineBlock(position);
            designation.removeFromDesignation(position);
            // TODO: move this into static attribute
            dwarf.getSkill().increaseSkillLevel(LaborTypeManager.getInstance().getLaborType("Mining"));
            // TODO: move this to dwarf to fix when dead bug
            dwarf.beIdleMovement();
            dwarf.releaseLock();
            done = true;
            break;
        }
    }
}
