package yadf.ui.swing.game.military;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import yadf.controller.AbstractController;
import yadf.controller.command.CancelJobCommand;
import yadf.controller.command.EnlistDwarfCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.job.IJob;
import yadf.simulation.military.IMilitaryManager;
import yadf.ui.swing.game.IGamePanel;
import yadf.ui.swing.game.guistate.MilitaryStationGuiState;

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
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /** The game panel. */
    private IGamePanel gamePanel;

    /**
     * Constructor.
     */
    public MilitaryPane() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        soldiersTable = new JTable();
        scrollPane.setViewportView(soldiersTable);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel, BorderLayout.WEST);

        JButton enlistButton = new JButton("Enlist");
        enlistButton.setMinimumSize(new Dimension(150, 23));
        enlistButton.setMaximumSize(new Dimension(150, 23));
        enlistButton.setPreferredSize(new Dimension(150, 23));
        enlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enlistButton.addActionListener(new EnlistButtonActionListener());
        panel.add(enlistButton);

        JButton btnStation = new JButton("Station");
        btnStation.setMinimumSize(new Dimension(150, 23));
        btnStation.setMaximumSize(new Dimension(150, 23));
        btnStation.setPreferredSize(new Dimension(150, 23));
        btnStation.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStation.addActionListener(new StationButtonActionListener());
        panel.add(btnStation);

        JButton btnCancelStation = new JButton("Cancel station");
        btnCancelStation.setMinimumSize(new Dimension(150, 23));
        btnCancelStation.setMaximumSize(new Dimension(150, 23));
        btnCancelStation.setPreferredSize(new Dimension(150, 23));
        btnCancelStation.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelStation.addActionListener(new CancelStationButtonActionListener());
        panel.add(btnCancelStation);
    }

    /**
     * Setup.
     * @param playerTmp the player
     * @param controllerTmp the controller
     * @param gamePanelTmp the game panel
     */
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp, final IGamePanel gamePanelTmp) {
        player = playerTmp;
        controller = controllerTmp;
        gamePanel = gamePanelTmp;
        soldiersTableModel = new SoldiersTableModel(player.getComponent(IMilitaryManager.class));
        soldiersTable.setModel(soldiersTableModel);
    }

    /**
     * Action listener for the enlist button.
     */
    private class EnlistButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            IGameCharacter[] dwarfs = player.getComponent(ICharacterManager.class).getCharacters()
                    .toArray(new IGameCharacter[0]);
            IGameCharacter dwarf = (IGameCharacter) JOptionPane.showInputDialog(gamePanel.getWorldPanel(), "Dwarf",
                    "Enlist dwarf", JOptionPane.QUESTION_MESSAGE, null, dwarfs, dwarfs[0]);
            if (dwarf == null) {
                return;
            }
            controller.addCommand(new EnlistDwarfCommand(player, dwarf.getId()));
        }
    }

    /**
     * Action listener for the station button.
     */
    private class StationButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            gamePanel.setState(new MilitaryStationGuiState());
        }
    }

    /**
     * Action listener for the cancel station button.
     */
    private class CancelStationButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            Set<IJob> jobs = player.getComponent(IMilitaryManager.class).getStationJobs();
            for (IJob job : jobs) {
                controller.addCommand(new CancelJobCommand(player, job.getId()));
            }
        }
    }
}
