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

import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.character.IMovementComponent;
import simulation.character.component.StillMovementComponent;

/**
 * The Class WasteTimeJob.
 */
public class WasteTimeJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 5295138993750863295L;

    /** The dwarf that is wasting time. */
    private final GameCharacter character;

    /** The amount of time to waste (simulation steps). */
    private final long duration;

    /** Simulation steps wasted so far. */
    private int simulationSteps = 0;

    /** Is the job done. */
    private boolean done = false;

    /**
     * Instantiates a new waste time job.
     * @param characterTmp the character
     * @param durationTmp the duration
     */
    public WasteTimeJob(final GameCharacter characterTmp, final long durationTmp) {
        character = characterTmp;
        duration = durationTmp;
        character.setComponent(IMovementComponent.class, new StillMovementComponent());
    }

    @Override
    public String getStatus() {
        return "Wasting time";
    }

    @Override
    public void interrupt(final String message) {
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "Wasting time";
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }
        simulationSteps++;
        if (simulationSteps > duration) {
            done = true;
        }
    }
}
