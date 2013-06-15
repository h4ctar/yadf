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
import simulation.Tree;
import simulation.character.Dwarf;
import simulation.character.component.IMovementComponent;
import simulation.character.component.ISkillComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.WasteTimeJob;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import simulation.map.MapIndex;

/**
 * The Class ChopTreeJob.
 */
public class ChopTreeJob extends AbstractDesignationJob {

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** The waiting for dwarf. */
        WAITING_FOR_DWARF,
        /** The goto. */
        GOTO,
        /** The chop tree. */
        CHOP_TREE
    }

    /** The serial version UID. */
    private static final long serialVersionUID = -7689577169283098909L;

    /** Amount of time to spend chopping down the tree (simulation steps). */
    private static final long DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /** The labor type required for this job. */
    private static final LaborType REQUIRED_LABOR = LaborTypeManager.getInstance().getLaborType("Wood cutting");

    /** The state. */
    private State state = State.WAITING_FOR_DWARF;

    /** The tree. */
    private final Tree tree;

    /** The designation. */
    private final ChopTreeDesignation designation;

    /** The move component. */
    private WalkMovementComponent moveComponent = null;

    /** Reference to the waste time job. */
    private WasteTimeJob wasteTimeJob;

    /** The dwarf. */
    private Dwarf dwarf;

    /** The done. */
    private boolean done = false;

    /**
     * Instantiates a new chop tree job.
     * @param treeTmp the tree
     * @param designationTmp the designation
     */
    public ChopTreeJob(final Tree treeTmp, final ChopTreeDesignation designationTmp) {
        tree = treeTmp;
        designation = designationTmp;
    }

    @Override
    public MapIndex getPosition() {
        return tree.getPosition();
    }

    @Override
    public String getStatus() {
        return "Chopping tree";
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);
        designation.removeFromDesignation(tree.getPosition());

        if (wasteTimeJob != null) {
            wasteTimeJob.interrupt("Chop tree job was interrupted");
        }

        if (dwarf != null) {
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
        return "Chop down tree";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAITING_FOR_DWARF:
            dwarf = player.getDwarfManager().getIdleDwarf(REQUIRED_LABOR);
            if (dwarf != null) {
                moveComponent = new WalkMovementComponent(tree.getPosition(), false);
                dwarf.setComponent(IMovementComponent.class, moveComponent);
                state = State.GOTO;
            }
            break;

        case GOTO:
            if (tree == null || tree.getRemove()) {
                interrupt("Tree missing");
                return;
            }
            if (moveComponent.isNoPath()) {
                interrupt("No path to tree");
                return;
            }
            if (moveComponent.isArrived()) {
                state = State.CHOP_TREE;
                wasteTimeJob = new WasteTimeJob(dwarf, DURATION);
            }
            break;

        case CHOP_TREE:
            wasteTimeJob.update(player, region);
            if (wasteTimeJob.isDone()) {
                designation.removeFromDesignation(tree.getPosition());
                if (tree == null || tree.getRemove()) {
                    interrupt("Tree missing");
                    return;
                }
                ItemType itemType = ItemTypeManager.getInstance().getItemType("Log");
                Item log = ItemTypeManager.getInstance().createItem(tree.getPosition(), itemType, player);
                player.getStockManager().addItem(log);
                tree.setRemove();
                dwarf.getComponent(ISkillComponent.class).increaseSkillLevel(REQUIRED_LABOR);
                dwarf.releaseLock();
                done = true;
            }
            break;

        default:
            break;
        }
    }
}
