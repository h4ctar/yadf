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
package userinterface.labor;

import javax.swing.table.AbstractTableModel;

import logger.Logger;
import simulation.AbstractGameObject;
import simulation.IPlayerListener;
import simulation.Player;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.character.component.IComponentListener;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;
import controller.AbstractController;
import controller.command.EnableLaborCommand;

/**
 * The Class LaborTableModel.
 */
public class LaborTableModel extends AbstractTableModel implements IPlayerListener, IComponentListener {

    /** The player. */
    private final Player player;

    /** The controller. */
    private final AbstractController controller;

    /**
     * Instantiates a new labor table model.
     * 
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public LaborTableModel(final Player playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;
        player.addListener(this);
        for (Dwarf dwarf : player.getDwarfs()) {
            dwarf.getSkill().addListener(this);
        }
    }

    @Override
    public void componentChanged() {
        fireTableRowsUpdated(0, getRowCount() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }

        return LaborNode.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return 1 + LaborTypeManager.getInstance().getLaborTypes().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "Dwarf name";
        }
        LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
        return laborType.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        if (player == null) {
            return 0;
        }

        return player.getDwarfs().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Dwarf dwarf = player.getDwarfs().get(rowIndex);

        if (columnIndex == 0) {
            LaborType profession = dwarf.getSkill().getProfession();
            String professionString = "";
            if (profession != null) {
                professionString = " (" + profession.professionName + ")";
            }
            return dwarf.getName() + professionString;
        }

        if (dwarf.isDead()) {
            return null;
        }
        LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
        Integer skill = new Integer(dwarf.getSkill().getLaborSkill(laborType));
        Boolean enabled = new Boolean(dwarf.getSkill().isLaborEnabled(laborType));

        return new LaborNode(skill, enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(final int row, final int col) {
        if (col == 0) {
            return false;
        }

        return true;
    }

    @Override
    public void playerChanged(final AbstractGameObject gameObject, final boolean added) {
        Logger.getInstance().log(this, "playerChanged");
        if (gameObject instanceof Dwarf) {
            Dwarf dwarf = (Dwarf) gameObject;
            dwarf.getSkill().addListener(this);
            fireTableDataChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (columnIndex != 0) {
            GameCharacter dwarf = player.getDwarfs().get(rowIndex);
            LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
            boolean enabled = ((Boolean) aValue).booleanValue();
            controller.addCommand(new EnableLaborCommand(player, dwarf.getId(), laborType.name, enabled));
        }
    }
}
