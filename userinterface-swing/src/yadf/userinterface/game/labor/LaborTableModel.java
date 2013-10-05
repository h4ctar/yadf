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
package yadf.userinterface.game.labor;

import javax.swing.table.AbstractTableModel;

import yadf.controller.AbstractController;
import yadf.controller.command.EnableLaborCommand;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.IPlayer;
import yadf.simulation.character.Dwarf;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.ICharacterComponentListener;
import yadf.simulation.character.component.ISkillComponent;
import yadf.simulation.labor.LaborType;
import yadf.simulation.labor.LaborTypeManager;

/**
 * The Class LaborTableModel.
 */
class LaborTableModel extends AbstractTableModel implements IGameObjectManagerListener,
        ICharacterComponentListener {

    /** The serial version UID. */
    private static final long serialVersionUID = -2428815575025895385L;

    /** The player. */
    private final IPlayer player;

    /** The controller. */
    private final AbstractController controller;

    /**
     * Instantiates a new labor table model.
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    LaborTableModel(final IPlayer playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;
        player.getComponent(ICharacterManager.class).addGameObjectManagerListener(this);
        for (IGameCharacter dwarf : player.getComponent(ICharacterManager.class).getCharacters()) {
            dwarf.getComponent(ISkillComponent.class).addListener(this);
        }
        // TODO: remove dwarf manager listener
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }

        return LaborNode.class;
    }

    @Override
    public int getColumnCount() {
        return 1 + LaborTypeManager.getInstance().getLaborTypes().size();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "Dwarf name";
        }
        LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
        return laborType.name;
    }

    @Override
    public int getRowCount() {
        if (player == null) {
            return 0;
        }
        return player.getComponent(ICharacterManager.class).getCharacters().size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Dwarf dwarf = player.getComponent(ICharacterManager.class).getCharacters().toArray(new Dwarf[0])[rowIndex];

        if (columnIndex == 0) {
            LaborType profession = dwarf.getComponent(ISkillComponent.class).getProfession();
            String professionString = " (" + profession.professionName + ")";
            return dwarf.getName() + professionString;
        }

        if (dwarf.isDead()) {
            return null;
        }
        LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
        Integer skill = new Integer(dwarf.getComponent(ISkillComponent.class).getLaborSkill(laborType));
        Boolean enabled = new Boolean(dwarf.getComponent(ISkillComponent.class).isLaborEnabled(laborType));

        return new LaborNode(skill, enabled);
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        if (col == 0) {
            return false;
        }

        return true;
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (columnIndex != 0) {
            Dwarf dwarf = player.getComponent(ICharacterManager.class).getCharacters().toArray(new Dwarf[0])[rowIndex];
            LaborType laborType = (LaborType) LaborTypeManager.getInstance().getLaborTypes().toArray()[columnIndex - 1];
            boolean enabled = ((Boolean) aValue).booleanValue();
            controller.addCommand(new EnableLaborCommand(player, dwarf.getId(), laborType.name, enabled));
        }
    }

    @Override
    public void componentChanged(final Object component) {
        assert component instanceof ISkillComponent;
        fireTableRowsUpdated(0, getRowCount() - 1);
        // TODO: fix this
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject, final int index) {
        assert gameObject instanceof IGameCharacter;
        IGameCharacter dwarf = (IGameCharacter) gameObject;
        dwarf.getComponent(ISkillComponent.class).addListener(this);
        fireTableRowsInserted(index, index);
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject, final int index) {
        assert gameObject instanceof IGameCharacter;
        IGameCharacter dwarf = (IGameCharacter) gameObject;
        dwarf.getComponent(ISkillComponent.class).removeListener(this);
        fireTableRowsDeleted(index, index);
    }

    /**
     * Get the dwarf at row.
     * @param row the row
     * @return the dwarf
     */
    public IGameCharacter getDwarf(final int row) {
        return player.getComponent(ICharacterManager.class).getCharacters().toArray(new Dwarf[0])[row];
    }
}
