package userinterface.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import simulation.item.ContainerItem;
import simulation.item.Item;
import userinterface.SpriteManager;
import userinterface.components.ImagePanel;
import userinterface.components.OutlineLabel;

/**
 * Interface for an item.
 */
public class ItemInterface extends JInternalFrame {

    /** The serial version UID. */
    private static final long serialVersionUID = -5779376341490166019L;

    /** The size in pixels of the item image. */
    private static final int IMAGE_SIZE = 200;

    /** The type text field. */
    private JTextField typeTextField;

    /** The used text field. */
    private JTextField usedTextField;

    /** The placed text field. */
    private JTextField placedTextField;

    /** The panel text field. */
    protected JPanel panel;

    /** The contents label. */
    private JLabel contentsLabel;

    /** The contents list. */
    private JList<String> contentsList;

    /** The contents scroll pane. */
    private JScrollPane scrollPane;

    /** The image of the item. */
    private JLabel imageLabel;

    /**
     * Constructor.
     */
    public ItemInterface() {
        setupLayout();
    }

    /**
     * Set the item that this interface is showing.
     * @param item the item
     */
    public void setItem(final Item item) {
        typeTextField.setText(item.getType().toString());
        usedTextField.setText(Boolean.toString(item.isUsed()));
        placedTextField.setText(Boolean.toString(item.isPlaced()));
        if (item instanceof ContainerItem) {
            contentsLabel.setVisible(true);
            scrollPane.setVisible(true);
            List<Item> contents = ((ContainerItem) item).getContentItems();
            String[] contentStrings = new String[contents.size()];
            for (int i = 0; i < contents.size(); i++) {
                contentStrings[i] = contents.get(i).toString();
            }
            contentsList.setListData(contentStrings);
        } else {
            contentsLabel.setVisible(false);
            scrollPane.setVisible(false);
        }
        Image itemImage = SpriteManager.getInstance().getItemSprite(item.getType().sprite).getImage();
        itemImage = itemImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST);
        imageLabel.setIcon(new ImageIcon(itemImage));
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setClosable(true);
        setTitle("Item interface");
        setBounds(100, 100, 608, 329);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setResizable(true);

        panel = new ImagePanel();
        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 252, 0, 0, 0 };
        panelLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        panelLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(panelLayout);
        getContentPane().add(panel, BorderLayout.CENTER);

        imageLabel = new JLabel("");
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        GridBagConstraints gbc_imageLabel = new GridBagConstraints();
        gbc_imageLabel.gridheight = 4;
        gbc_imageLabel.insets = new Insets(0, 0, 0, 5);
        gbc_imageLabel.gridx = 0;
        gbc_imageLabel.gridy = 0;
        panel.add(imageLabel, gbc_imageLabel);

        JLabel typeLabel = new OutlineLabel("Type:");
        typeLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_typeLabel = new GridBagConstraints();
        gbc_typeLabel.anchor = GridBagConstraints.EAST;
        gbc_typeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_typeLabel.gridx = 1;
        gbc_typeLabel.gridy = 0;
        panel.add(typeLabel, gbc_typeLabel);

        typeTextField = new JTextField();
        typeTextField.setEditable(false);
        GridBagConstraints gbc_typeTextField = new GridBagConstraints();
        gbc_typeTextField.insets = new Insets(0, 0, 5, 0);
        gbc_typeTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_typeTextField.gridx = 2;
        gbc_typeTextField.gridy = 0;
        panel.add(typeTextField, gbc_typeTextField);
        typeTextField.setColumns(10);

        JLabel usedLabel = new OutlineLabel("Used:");
        usedLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_usedLabel = new GridBagConstraints();
        gbc_usedLabel.anchor = GridBagConstraints.EAST;
        gbc_usedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_usedLabel.gridx = 1;
        gbc_usedLabel.gridy = 1;
        panel.add(usedLabel, gbc_usedLabel);

        usedTextField = new JTextField();
        usedTextField.setEditable(false);
        GridBagConstraints gbc_usedTextField = new GridBagConstraints();
        gbc_usedTextField.insets = new Insets(0, 0, 5, 0);
        gbc_usedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_usedTextField.gridx = 2;
        gbc_usedTextField.gridy = 1;
        panel.add(usedTextField, gbc_usedTextField);
        usedTextField.setColumns(10);

        JLabel placedLabel = new OutlineLabel("Placed:");
        placedLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_placedLabel = new GridBagConstraints();
        gbc_placedLabel.anchor = GridBagConstraints.EAST;
        gbc_placedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_placedLabel.gridx = 1;
        gbc_placedLabel.gridy = 2;
        panel.add(placedLabel, gbc_placedLabel);

        placedTextField = new JTextField();
        placedTextField.setEditable(false);
        GridBagConstraints gbc_placedTextField = new GridBagConstraints();
        gbc_placedTextField.insets = new Insets(0, 0, 5, 0);
        gbc_placedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_placedTextField.gridx = 2;
        gbc_placedTextField.gridy = 2;
        panel.add(placedTextField, gbc_placedTextField);
        placedTextField.setColumns(10);

        contentsLabel = new OutlineLabel("Contents:");
        GridBagConstraints gbc_lblContents = new GridBagConstraints();
        gbc_lblContents.fill = GridBagConstraints.VERTICAL;
        gbc_lblContents.insets = new Insets(0, 0, 0, 5);
        gbc_lblContents.gridx = 1;
        gbc_lblContents.gridy = 3;
        panel.add(contentsLabel, gbc_lblContents);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 2;
        gbc_scrollPane.gridy = 3;
        panel.add(scrollPane, gbc_scrollPane);

        contentsList = new JList<>();
        scrollPane.setViewportView(contentsList);
        // CHECKSTYLE:ON
    }
}
