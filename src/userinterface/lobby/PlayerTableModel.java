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
package userinterface.lobby;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * The Class PlayerTableModel.
 */
public class PlayerTableModel extends AbstractTableModel {

    /** The serial version UID. */
    private static final long serialVersionUID = -3374765986710593510L;

    /** The player names. */
    private List<String> playerNames = new ArrayList<>();

    /** The player index. */
    private int playerIndex;

    /**
     * Adds the player.
     * 
     * @param string the string
     */
    public void addPlayer(final String string) {
        playerNames.add(string);
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "Player Number";
        }

        return "Player Name";
    }

    /**
     * Gets the player index.
     * 
     * @return the player index
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Gets the player name.
     * 
     * @return the player name
     */
    public String getPlayerName() {
        return playerNames.get(playerIndex);
    }

    /**
     * Gets the player names.
     * 
     * @return the player names
     */
    public List<String> getPlayerNames() {
        return playerNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return playerNames.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return new Integer(rowIndex);
        }

        return playerNames.get(rowIndex);
    }

    /**
     * Sets the player index.
     * 
     * @param index the new player index
     */
    public void setPlayerIndex(final int index) {
        playerIndex = index;
    }

    /**
     * Sets the player name.
     * 
     * @param playerName the player name
     * @param playerIndexTmp the player index
     */
    public void setPlayerName(final String playerName, final int playerIndexTmp) {
        playerNames.set(playerIndexTmp, playerName);
        fireTableDataChanged();
    }

    /**
     * Sets the player names.
     * 
     * @param playerNamesTmp the new player names
     */
    public void setPlayerNames(final List<String> playerNamesTmp) {
        playerNames = playerNamesTmp;
        fireTableDataChanged();
    }
}
