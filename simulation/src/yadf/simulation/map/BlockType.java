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
package yadf.simulation.map;

/**
 * The different types of blocks.
 * 
 * @author Ben Smith (bensmith87@gmail.com)
 */
public enum BlockType {
    /* isStandIn isStandOn isClimb isMineable itemMined sprite */
    /** The empty. */
    EMPTY(true, false, false, false, null, 1),

    /** The grass. */
    GRASS(false, true, false, true, null, 22),

    /** The dirt. */
    DIRT(false, true, false, true, null, 29),

    /** The rock. */
    ROCK(false, true, false, true, "Rock", 28),

    /** The gold. */
    GOLD(false, true, false, true, "Gold ore", 30),

    /** The iron. */
    IRON(false, true, false, true, "Iron ore", 32),

    /** The coal. */
    COAL(false, true, false, true, "Coal ore", 33),

    /** The ramp. */
    RAMP(true, true, true, false, null, 26),

    /** The stair. */
    STAIR(true, true, true, false, null, 27),

    /** The wall. */
    WALL(false, true, false, false, null, 0),

    /** The mine grass. */
    MINE(true, true, false, false, null, GRASS.sprite);

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

    /** The sprite. */
    public final int sprite;

    /**
     * Instantiates a new block type.
     * 
     * @param isStandInTmp the is stand in
     * @param isStandOnTmp the is stand on
     * @param isClimbTmp the is climb
     * @param isMineableTmp the is mineable
     * @param itemMinedTmp the item mined
     * @param spriteTmp the sprite
     */
    BlockType(final boolean isStandInTmp, final boolean isStandOnTmp, final boolean isClimbTmp,
            final boolean isMineableTmp, final String itemMinedTmp, final int spriteTmp) {
        isStandIn = isStandInTmp;
        isStandOn = isStandOnTmp;
        isClimb = isClimbTmp;
        isMineable = isMineableTmp;
        sprite = spriteTmp;
        itemMined = itemMinedTmp;
        isSolid = isStandOn && !isStandIn;
    }
}
