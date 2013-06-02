package userinterface.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestPanel extends JPanel {
    /** The serial version UID. */
    private static final long serialVersionUID = -1897222568833998126L;

    public TestPanel() {
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel lblNewLabel = new JLabel("New label");
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("New label");
        panel.add(lblNewLabel_1);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.BLUE);
        add(panel_1, BorderLayout.CENTER);
    }

}
