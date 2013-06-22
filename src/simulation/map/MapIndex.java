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
 * The Class MapIndex.
 */
public class MapIndex implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = -8302537732545902448L;

    /** The coordinates. */
    public int x, y, z;

    /**
     * Instantiates a new map index, (0, 0, 0).
     */
    public MapIndex() {
        /* do nothing. */
    }

    /**
     * Instantiates a new map index.
     * @param xTmp the x coordinate
     * @param yTmp the y coordinate
     * @param zTmp the z coordinate
     */
    public MapIndex(final int xTmp, final int yTmp, final int zTmp) {
        x = xTmp;
        y = yTmp;
        z = zTmp;
    }

    /**
     * Instantiates a new map index as a copy of another map index.
     * @param old the map index to clone
     */
    public MapIndex(final MapIndex old) {
        x = old.x;
        y = old.y;
        z = old.z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Adds an offset to the map index and returns the result.
     * @param xTmp the x
     * @param yTmp the y
     * @param zTmp the z
     * @return the result
     */
    public MapIndex add(final int xTmp, final int yTmp, final int zTmp) {
        MapIndex a = new MapIndex();
        a.x = x + xTmp;
        a.y = y + yTmp;
        a.z = z + zTmp;
        return a;
    }

    /**
     * Adds two map indicies together and returns the result.
     * @param b the b
     * @return the result
     */
    public MapIndex add(final MapIndex b) {
        MapIndex a = new MapIndex();
        a.x = x + b.x;
        a.y = y + b.y;
        a.z = z + b.z;
        return a;
    }

    /**
     * Calculates the Manhattan distance between this map index and another.
     * @param pos the second position
     * @return the distance
     */
    public int distance(final MapIndex pos) {
        return Math.abs(x - pos.x) + Math.abs(y - pos.y) + Math.abs(z - pos.z);
    }

    /**
     * Subtract a map index from this map index and returns the result.
     * @param b the map index to subtract
     * @return the result
     */
    public MapIndex sub(final MapIndex b) {
        MapIndex a = new MapIndex();
        a.x = x - b.x;
        a.y = y - b.y;
        a.z = z - b.z;
        return a;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapIndex)) {
            return false;
        }
        MapIndex other = (MapIndex) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.x;
        result = prime * result + this.y;
        result = prime * result + this.z;
        return result;
    }
}
