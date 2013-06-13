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

import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.character.IInventoryComponent;
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
     * Drop haul item.
     * @param freeItem the free item
     */
    public void dropHaulItem(final boolean freeItem) {
        if (itemHauling != null) {
            if (freeItem) {
                itemHauling.setUsed(false);
            }
            itemHauling = null;
            notifyListeners();
        }
    }

    /**
     * Drop tool.
     */
    public void dropTool() {
        if (toolHolding != null) {
            toolHolding.setUsed(false);
            toolHolding = null;
            notifyListeners();
        }
    }

    /**
     * Gets the haul item.
     * @return the haul item
     */
    public Item getHaulItem() {
        return itemHauling;
    }

    /**
     * Gets the tool holding.
     * @return the tool holding
     */
    public Item getToolHolding() {
        return toolHolding;
    }

    @Override
    public void kill() {
        dropHaulItem(true);
        dropTool();
    }

    /**
     * Pickup haul item.
     * @param item the item
     */
    public void pickupHaulItem(final Item item) {
        if (itemHauling != null) {
            dropHaulItem(true);
        }

        item.setUsed(true);
        itemHauling = item;
        notifyListeners();
    }

    /**
     * Pickup tool.
     * @param tool the tool
     */
    public void pickupTool(final Item tool) {
        if (toolHolding != null) {
            dropTool();
        }

        tool.setUsed(true);
        toolHolding = tool;
        notifyListeners();
    }

    @Override
    public void update(final GameCharacter character, final Player player, final Region region) {
        if (itemHauling != null) {
            itemHauling.setPosition(character.getPosition());
        }

        if (toolHolding != null) {
            toolHolding.setPosition(character.getPosition());
        }
    }
}
