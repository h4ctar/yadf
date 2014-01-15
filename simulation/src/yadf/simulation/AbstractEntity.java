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
package yadf.simulation;

import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * The Class Entity.
 */
public abstract class AbstractEntity extends AbstractGameObject implements IEntity {

    /** The area of the entity. */
    private MapArea area;

    /**
     * Instantiates a new entity.
     * @param positionTmp the position
     */
    public AbstractEntity(final MapIndex positionTmp) {
        area = new MapArea(new MapIndex(positionTmp), 1, 1);
    }

    public AbstractEntity(MapArea areaTmp) {
        area = new MapArea(areaTmp);
    }

    public AbstractEntity(MapIndex position, int width, int height) {
        area = new MapArea(position, width, height);
    }

    @Override
    public MapIndex getPosition() {
        return new MapIndex(area.pos);
    }

    @Override
    public void setPosition(final MapIndex positionTmp) {
        area.pos.x = positionTmp.x;
        area.pos.y = positionTmp.y;
        area.pos.z = positionTmp.z;
    }

    @Override
    public MapArea getArea() {
        return new MapArea(area);
    }

    @Override
    public boolean containsIndex(final MapIndex index) {
        MapIndex pos = getPosition();
        return index.x >= pos.x && index.x <= pos.x + area.width - 1 && index.y >= pos.y
                && index.y <= pos.y + area.height - 1 && pos.z == index.z;
    }
}
