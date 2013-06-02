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

import simulation.Region;
import simulation.map.BlockType;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;

/**
 * The Class ChannelDesignation.
 */
public class ChannelDesignation extends AbstractDesignation {

    /** The serial version UID. */
    private static final long serialVersionUID = 6420960356264271866L;

    /** The block type. */
    private final BlockType blockType;

    /**
     * Instantiates a new channel designation.
     * @param blockTypeTmp the block type
     */
    public ChannelDesignation(final BlockType blockTypeTmp) {
        blockType = blockTypeTmp;
    }

    @Override
    public String toString() {
        return "Channel Designation";
    }

    @Override
    public boolean valid(final MapIndex mapIndex, final Region region) {
        RegionMap map = region.getMap();

        return region.checkAreaValid(new MapArea(mapIndex, 1, 1)) && map.getBlock(mapIndex.add(0, 0, -1)).isMineable;
    }

    @Override
    protected AbstractDesignationJob createJob(final MapIndex mapIndex, final Region region) {
        return new ChannelJob(mapIndex, blockType, this);
    }
}
