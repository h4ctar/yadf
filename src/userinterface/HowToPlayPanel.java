package userinterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import userinterface.components.ImagePanel;
import userinterface.lobby.IMainWindow;

/**
 * A panel that describes how to play the game.
 */
public class HowToPlayPanel extends ImagePanel {

    /** The serial version UID. */
    private static final long serialVersionUID = -3556881351108569221L;

    /** The main window that this panel belongs to. */
    private final IMainWindow mainWindow;

    /**
     * Constructor.
     * @param mainWindowTmp the main window that this panel belongs to
     */
    public HowToPlayPanel(final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        setupLayout();
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        JLabel howToPlayLabel = new JLabel("Single Player Game");
        howToPlayLabel.setText("How to Play");
        howToPlayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        howToPlayLabel.setForeground(Color.WHITE);
        howToPlayLabel.setFont(new Font("Minecraftia", Font.PLAIN, 40));
        GridBagConstraints gbc_howToPlayLabel = new GridBagConstraints();
        gbc_howToPlayLabel.insets = new Insets(0, 0, 5, 5);
        gbc_howToPlayLabel.gridx = 1;
        gbc_howToPlayLabel.gridy = 0;
        add(howToPlayLabel, gbc_howToPlayLabel);

        JTextArea howToPlayTextArea = new JTextArea();
        howToPlayTextArea
                .setText("w, s, a & d to pan view\r\nq and e to move view up and down\r\nright click for context menu\r\nhold shift while taking action to be able to repeat it\r\nclick dwarf for details\r\nclick stockpile to change what it accepts\r\nclick a workshop to add orders\r\nuse tabs to view jobs, stock and change the labors of each dwarf");
        GridBagConstraints gbc_howToPlayTextArea = new GridBagConstraints();
        gbc_howToPlayTextArea.insets = new Insets(0, 0, 5, 5);
        gbc_howToPlayTextArea.fill = GridBagConstraints.BOTH;
        gbc_howToPlayTextArea.gridx = 1;
        gbc_howToPlayTextArea.gridy = 1;
        add(howToPlayTextArea, gbc_howToPlayTextArea);

        JButton backButton = new JButton("Back");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 0, 5, 5);
        gbc_backButton.gridx = 1;
        gbc_backButton.gridy = 2;
        backButton.addActionListener(new BackButtonActionListener());
        add(backButton, gbc_backButton);
        // ON
    }

    private class BackButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainWindow.setupMainMenu();
        }
    }
}
