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
package yadf.controller.command;

import yadf.simulation.IPlayer;

/**
 * The Class SetStockpileCommand.
 */
public class SetStockpileCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = 4663011796429573553L;

    /** The stockpile id. */
    private final int stockpileId;

    /** The item type name. */
    private final String itemTypeName;

    /** The add. */
    private final boolean accept;

    /**
     * Instantiates a new sets the stockpile command.
     * @param player the player
     * @param stockpileIdTmp the stockpile id
     * @param itemTypeNameTmp the item type name
     * @param acceptTmp true to accept the item type
     */
    public SetStockpileCommand(final IPlayer player, final int stockpileIdTmp, final String itemTypeNameTmp,
            final boolean acceptTmp) {
        super(player);
        stockpileId = stockpileIdTmp;
        itemTypeName = itemTypeNameTmp;
        accept = acceptTmp;
    }

    @Override
    public void doCommand() {
        // Stockpile stockpile = player.getComponent(IStockManager.class).getStockpileManager()
        // .getGameObject(stockpileId);
        // stockpile.setItemType(itemTypeName, accept);
    }
}
