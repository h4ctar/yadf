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
package userinterface.game.room;

import javax.swing.AbstractListModel;

import simulation.IGameObject;
import simulation.IGameObjectManagerListener;
import simulation.item.Item;
import simulation.room.Room;

/**
 * The Class ItemListModel.
 */
class ItemListModel extends AbstractListModel<String> implements IGameObjectManagerListener {

    /** The serial version UID. */
    private static final long serialVersionUID = -1108175675511497032L;

    /** The room. */
    private Room room;

    /**
     * Sets the room.
     * @param roomTmp the new room
     */
    public void setRoom(final Room roomTmp) {
        if (room != null) {
            room.removeGameObjectManagerListener(this);
        }
        room = roomTmp;
        room.addGameObjectManagerListener(this);
    }

    @Override
    public String getElementAt(final int index) {
        return room.getItems().get(index).toString();
    }

    @Override
    public int getSize() {
        int size = 0;
        if (room != null) {
            size = room.getItems().size();
        }
        return size;
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject, final int index) {
        assert gameObject instanceof Item;
        fireContentsChanged(this, index, index);
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject, final int index) {
        assert gameObject instanceof Item;
        fireContentsChanged(this, index, index);
    }
}
