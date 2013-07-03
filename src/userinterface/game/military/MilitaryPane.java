package userinterface.game.military;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulation.Player;
import simulation.character.IGameCharacter;
import controller.AbstractController;
import controller.command.EnlistDwarfCommand;

/**
 * The military pane.
 */
public class MilitaryPane extends JPanel {

    /** The default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The soldiers table. */
    private JTable soldiersTable;

    /** The soldiers table model. */
    private SoldiersTableModel soldiersTableModel;

    /** The player. */
    private Player player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     * @param controllerTmp the controller
     */
    public MilitaryPane() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        soldiersTable = new JTable();
        scrollPane.setViewportView(soldiersTable);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, BorderLayout.SOUTH);

        JButton enlistButton = new JButton("Enlist");
        enlistButton.addActionListener(new EnlistButtonActionListener());
        panel.add(enlistButton);
    }

    /**
     * Setup.
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public void setup(final Player playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;
        soldiersTableModel = new SoldiersTableModel(player.getMilitaryManager());
        soldiersTable.setModel(soldiersTableModel);
    }

    /**
     * Action listener for the enlist button.
     */
    private class EnlistButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            IGameCharacter[] dwarfs = player.getDwarfManager().getDwarfs().toArray(new IGameCharacter[0]);
            IGameCharacter dwarf = (IGameCharacter) JOptionPane.showInputDialog(MilitaryPane.this, "Dwarf",
                    "Enlist dwarf", JOptionPane.QUESTION_MESSAGE, null, dwarfs, dwarfs[0]);
            if (dwarf == null) {
                return;
            }
            controller.addCommand(new EnlistDwarfCommand(player, dwarf.getId()));
        }
    }
}
