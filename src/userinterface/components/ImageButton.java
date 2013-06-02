package userinterface.components;

import java.awt.Image;

import javax.swing.JButton;

public class ImageButton extends JButton {

    /** The serial version UID. */
    private static final long serialVersionUID = 4801838561842038893L;

    /** The image. */
    private Image image;

    /**
     * Instantiates a new image panel.
     */
    public ImageButton(String text) {
        super(text);
        setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("button.png")));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
}
