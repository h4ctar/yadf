package userinterface.game.military;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import simulation.character.IGameCharacter;
import simulation.military.IMilitaryManager;
import simulation.military.IMilitaryManagerListener;

/**
 * The soldiers table model.
 */
public class SoldiersTableModel extends AbstractTableModel implements IMilitaryManagerListener {

    private IMilitaryManager militaryManager;

    /**
     * Constructor.
     * @param militaryManagerTmp the military manager
     */
    public SoldiersTableModel(final IMilitaryManager militaryManagerTmp) {
        militaryManager = militaryManagerTmp;
        militaryManager.addMilitaryManagerListener(this);
        // TODO: remove military manager listener
    }

    @Override
    public int getRowCount() {
        Set<IGameCharacter> soldiers = militaryManager.getSoldiers();
        return soldiers.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return "Soldier name";
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        IGameCharacter[] soldiers = militaryManager.getSoldiers().toArray(new IGameCharacter[0]);
        return soldiers[rowIndex];
    }

    @Override
    public void soldierAdded() {
        fireTableDataChanged();
    }

    @Override
    public void soldierRemoved() {
        // TODO Auto-generated method stub
    }
}
