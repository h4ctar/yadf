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

import java.io.Serializable;

/**
 * The Class MapArea.
 */
public class MapArea implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 8733787812832562888L;

    /** The pos. */
    public MapIndex pos;

    /** The width. */
    public int width;

    /** The height. */
    public int height;

    /**
     * Instantiates a new map area.
     */
    public MapArea() {
        this.pos = new MapIndex();
        this.width = 1;
        this.height = 1;
    }

    /**
     * Instantiates a new map area.
     * @param old the old
     */
    public MapArea(final MapArea old) {
        this.pos = new MapIndex(old.pos);
        this.width = old.width;
        this.height = old.height;
    }

    /**
     * Instantiates a new map area.
     * @param posTmp the pos
     * @param widthTmp the width
     * @param heightTmp the height
     */
    public MapArea(final MapIndex posTmp, final int widthTmp, final int heightTmp) {
        pos = posTmp;
        width = widthTmp;
        height = heightTmp;
    }

    public boolean containesIndex(MapIndex index) {
        return (index.x < pos.x + width) && (index.x >= pos.x) && (index.y < pos.y + height) && (index.y >= pos.y)
                && (index.z == pos.z);
    }

    public boolean operlapsArea(MapArea area) {
        return (area.pos.x < pos.x + width) && (area.pos.x + area.width > pos.x) && (area.pos.y < pos.y + height)
                && (area.pos.y + area.height > pos.y) && (area.pos.z == pos.z);
    }
}
