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
package yadf.simulation.item;

import yadf.simulation.AbstractGameObject;
import yadf.simulation.IGameObjectAvailableListener;
import yadf.simulation.IGameObjectManagerListener;

/**
 * The Class StockManager, manages items for a player including stockpiles.
 */
public class StockManager extends AbstractGameObject implements IStockManager {

    /** The unstored items. */
    private final ItemManager unstoredItemManager = new ItemManager();

    /** The Stockpile Manager. */
    private final IStockpileManager stockpileManager = new StockpileManager();

    @Override
    public void addGameObject(final Item item) {
        unstoredItemManager.addGameObject(item);
    }

    @Override
    public void removeGameObject(final Item item) {
        if (unstoredItemManager.getGameObject(item.getId()) != null) {
            unstoredItemManager.removeGameObject(item);
        } else {
            stockpileManager.removeItem(item);
        }
    }

    @Override
    public Item getItem(final String itemTypeName, final boolean placed) {
        Item foundItem = unstoredItemManager.getItem(itemTypeName, placed);
        if (foundItem == null && !placed) {
            foundItem = stockpileManager.getItem(itemTypeName);
        }
        return foundItem;
    }

    @Override
    public Item getItemFromCategory(final String category, final boolean placed) {
        Item foundItem = unstoredItemManager.getItemFromCategory(category, placed);
        if (foundItem == null && !placed) {
            foundItem = stockpileManager.getItemFromCategory(category);
        }
        return foundItem;
    }

    @Override
    public int getItemQuantity(final String category) {
        int count = unstoredItemManager.getItemQuantity(category);
        count += stockpileManager.getItemQuantity(category);
        return count;
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        int count = unstoredItemManager.getItemQuantity(itemType);
        count += stockpileManager.getItemQuantity(itemType);
        return count;
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public IItemManager getUnstoredItemManager() {
        return unstoredItemManager;
    }

    @Override
    public IStockpileManager getStockpileManager() {
        return stockpileManager;
    }

    @Override
    public void addManagerListener(final IGameObjectManagerListener listener) {
        unstoredItemManager.addManagerListener(listener);
    }

    @Override
    public void removeManagerListener(final IGameObjectManagerListener listener) {
        unstoredItemManager.removeManagerListener(listener);
    }

    @Override
    public void addAvailableListener(final IGameObjectAvailableListener listener) {
        unstoredItemManager.addAvailableListener(listener);
    }

    @Override
    public void removeAvailableListener(final IGameObjectAvailableListener listener) {
        unstoredItemManager.removeAvailableListener(listener);
    }

    @Override
    public Item getGameObject(final int id) {
        Item foundItem = unstoredItemManager.getGameObject(id);
        if (foundItem == null) {
            foundItem = stockpileManager.getItem(id);
        }
        return foundItem;
    }
}
