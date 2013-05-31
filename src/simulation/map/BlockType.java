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
package simulation.map;

/**
 * The different types of blocks.
 * 
 * @author Ben Smith (bensmith87@gmail.com)
 */
public enum BlockType {
    /* isStandIn isStandOn isClimb isMineable itemMined */
    /** The empty. */
    EMPTY(true, false, false, false, null),

    /** The grass. */
    GRASS(false, true, false, true, null),

    /** The dirt. */
    DIRT(false, true, false, true, null),

    /** The rock. */
    ROCK(false, true, false, true, "Rock"),

    /** The gold. */
    GOLD(false, true, false, true, "Gold ore"),

    /** The iron. */
    IRON(false, true, false, true, "Iron ore"),

    /** The coal. */
    COAL(false, true, false, true, "Coal ore"),

    /** The ramp. */
    RAMP(true, true, true, false, null),

    /** The stair. */
    STAIR(true, true, true, false, null),

    /** The wall. */
    WALL(false, true, false, false, null),

    /** The mine grass. */
    MINE_GRASS(true, true, false, false, null),

    /** The mine dirt. */
    MINE_DIRT(true, true, false, false, null),

    /** The mine rock. */
    MINE_ROCK(true, true, false, false, null),

    /** The mine gold. */
    MINE_GOLD(true, true, false, false, null),

    /** The mine iron. */
    MINE_IRON(true, true, false, false, null),

    /** The mine coal. */
    MINE_COAL(true, true, false, false, null);

    /** The is stand in. */
    public final boolean isStandIn;

    /** The is stand on. */
    public final boolean isStandOn;

    /** The is climb. */
    public final boolean isClimb;

    /** The is solid. */
    public final boolean isSolid;

    /** The is mineable. */
    public final boolean isMineable;

    /** What item is produced when mined. */
    public final String itemMined;

    /**
     * Instantiates a new block type.
     * 
     * @param isStandIn the is stand in
     * @param isStandOn the is stand on
     * @param isClimb the is climb
     * @param isMineable the is mineable
     * @param itemMined the item mined
     */
    BlockType(final boolean isStandIn, final boolean isStandOn, final boolean isClimb, final boolean isMineable,
            final String itemMined) {
        this.isStandIn = isStandIn;
        this.isStandOn = isStandOn;
        this.isClimb = isClimb;
        this.isMineable = isMineable;
        this.itemMined = itemMined;
        isSolid = isStandOn && !isStandIn;
    }
}