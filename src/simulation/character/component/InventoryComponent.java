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
package simulation.character.component;

import simulation.Region;
import simulation.character.IGameCharacter;
import simulation.item.Item;

/**
 * The Class InventoryComponent.
 */
public class InventoryComponent extends AbstractCharacterComponent implements IInventoryComponent {

    /** The item hauling. */
    private Item itemHauling;

    /** The tool holding. */
    private Item toolHolding;

    /**
     * Constructor.
     * @param characterTmp the character that this component belongs to
     */
    public InventoryComponent(final IGameCharacter characterTmp) {
        super(characterTmp);
    }

    @Override
    public Item getHaulItem() {
        return itemHauling;
    }

    @Override
    public Item getToolHolding() {
        return toolHolding;
    }

    @Override
    public void kill() {
        dropHaulItem(true);
        dropTool();
    }

    @Override
    public void pickupHaulItem(final Item item) {
        assert itemHauling == null;
        itemHauling = item;
        itemHauling.setUsed(true);
        notifyListeners();
    }

    @Override
    public void dropHaulItem(final boolean freeItem) {
        if (itemHauling != null) {
            if (freeItem) {
                itemHauling.setUsed(false);
            }
            itemHauling = null;
            notifyListeners();
        }
    }

    @Override
    public void pickupTool(final Item tool) {
        assert toolHolding == null;
        toolHolding = tool;
        getCharacter().getPlayer().getStockManager().removeItem(toolHolding);
        notifyListeners();
    }

    @Override
    public void dropTool() {
        if (toolHolding != null) {
            getCharacter().getPlayer().getStockManager().addItem(toolHolding);
            toolHolding = null;
            notifyListeners();
        }
    }

    @Override
    public void update(final Region region) {
        if (itemHauling != null) {
            itemHauling.setPosition(getCharacter().getPosition());
        }
        if (toolHolding != null) {
            toolHolding.setPosition(getCharacter().getPosition());
        }
    }
}
