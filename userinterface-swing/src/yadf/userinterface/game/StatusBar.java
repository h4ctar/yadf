package yadf.userinterface.game;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import yadf.simulation.Region;

/**
 * The status bar.
 */
class StatusBar extends JPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The fps label. */
    private JLabel fpsLabel;

    /** The date label. */
    private JLabel dateLabel;

    /** The state label. */
    private JLabel stateLabel;

    /**
     * Constructor.
     */
    public StatusBar() {
        Border paddingBorder = BorderFactory.createEmptyBorder(2, 10, 2, 10);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        fpsLabel = new JLabel("FPS");
        fpsLabel.setBorder(paddingBorder);
        fpsLabel.setForeground(Color.WHITE);
        add(fpsLabel);

        dateLabel = new JLabel("Date");
        dateLabel.setBorder(paddingBorder);
        dateLabel.setForeground(Color.WHITE);
        add(dateLabel);

        stateLabel = new JLabel("State");
        stateLabel.setBorder(paddingBorder);
        stateLabel.setForeground(Color.WHITE);
        add(stateLabel);
    }

    /**
     * Update the fields on the status bar.
     * @param gameLoop the game loop (to get the FPS)
     * @param region the region (to get the time)
     * @param stateName the name of the state that the GUI is in
     */
    void update(final GameLoop gameLoop, final Region region, final String stateName) {
        fpsLabel.setText("FPS:" + Long.toString(gameLoop.getFps()));
        dateLabel.setText(region.getTimeString());
        stateLabel.setText(stateName);
    }
}
