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

    /** The z. */
    public int x, y, z;

    /**
     * Instantiates a new map index.
     */
    public MapIndex() { /* nothing to do */
    }

    /**
     * Instantiates a new map index.
     * 
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public MapIndex(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Instantiates a new map index.
     * 
     * @param old the old
     */
    public MapIndex(final MapIndex old) {
        this.x = old.x;
        this.y = old.y;
        this.z = old.z;
    }

    /**
     * Adds the.
     * 
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the map index
     */
    public MapIndex add(final int x, final int y, final int z) {
        MapIndex a = new MapIndex();
        a.x = this.x + x;
        a.y = this.y + y;
        a.z = this.z + z;
        return a;
    }

    /**
     * Adds the.
     * 
     * @param b the b
     * @return the map index
     */
    public MapIndex add(final MapIndex b) {
        MapIndex a = new MapIndex();
        a.x = x + b.x;
        a.y = y + b.y;
        a.z = z + b.z;
        return a;
    }

    /**
     * Distance.
     * 
     * @param pos the pos
     * @return the int
     */
    public int distance(final MapIndex pos) {
        return Math.abs(x - pos.x) + Math.abs(y - pos.y) + Math.abs(z - pos.z);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.x;
        result = prime * result + this.y;
        result = prime * result + this.z;
        return result;
    }

    /**
     * Sub.
     * 
     * @param b the b
     * @return the map index
     */
    public MapIndex sub(final MapIndex b) {
        MapIndex a = new MapIndex();
        a.x = x - b.x;
        a.y = y - b.y;
        a.z = z - b.z;
        return a;
    }
}
