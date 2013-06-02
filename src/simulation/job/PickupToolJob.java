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
import simulation.character.GameCharacter;
import simulation.character.component.WalkMoveComponent;
import simulation.item.Item;

/**
 * The Class PickupToolJob.
 */
public class PickupToolJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = -8533807757453770766L;

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** The waiting for dwarf. */
        WAITING_FOR_DWARF,
        /** The walk to tool. */
        WALK_TO_TOOL
    }

    /** The state. */
    private State state = State.WAITING_FOR_DWARF;

    /** The character. */
    private final GameCharacter character;

    /** The tool. */
    private final Item tool;

    /** The move component. */
    private WalkMoveComponent moveComponent;

    /** The done. */
    private boolean done;

    /**
     * Instantiates a new pickup tool job.
     * @param characterTmp the character
     * @param toolTmp the tool
     */
    public PickupToolJob(final GameCharacter characterTmp, final Item toolTmp) {
        character = characterTmp;
        tool = toolTmp;
        tool.setUsed(true);
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAITING_FOR_DWARF:
            return "Waiting for dwarf to become free";
        case WALK_TO_TOOL:
            return "Walking to tool";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        Logger.getInstance().log(this, toString() + " has been canceled: " + message, true);

        tool.setUsed(false);

        character.releaseLock();

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Pickup tool";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAITING_FOR_DWARF:
            if (character.acquireLock()) {
                moveComponent = character.walkToPosition(tool.getPosition(), false);
                state = State.WALK_TO_TOOL;
            }
            break;

        case WALK_TO_TOOL:
            if (moveComponent.isDone()) {
                // TODO: make remember
                character.beIdleMovement();
                character.getInventory().pickupTool(tool);
                character.releaseLock();

                done = true;
            }
            break;

        default:
            break;
        }
    }
}
