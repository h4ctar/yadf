package userinterface.game.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import simulation.item.ContainerItem;
import simulation.item.Item;
import userinterface.misc.SpriteManager;

/**
 * Interface for an item.
 */
public class ItemInterface extends JPanel {

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
        setOpaque(false);
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
            Set<Item> contentItems = ((ContainerItem) item).getItems();
            String[] contentStrings = new String[contentItems.size()];
            int i = 0;
            for (Item contentItem : contentItems) {
                contentStrings[i++] = contentItem.toString();
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
        setLayout(new BorderLayout(0, 0));
        panel = new JPanel();
        panel.setOpaque(false);
        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 0, 0 };
        panelLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        panelLayout.columnWeights = new double[] { 0.0, 1.0, };
        panelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0 };
        panel.setLayout(panelLayout);
        add(panel);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_typeLabel = new GridBagConstraints();
        gbc_typeLabel.anchor = GridBagConstraints.EAST;
        gbc_typeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_typeLabel.gridx = 0;
        gbc_typeLabel.gridy = 0;
        panel.add(typeLabel, gbc_typeLabel);

        typeTextField = new JTextField();
        typeTextField.setEditable(false);
        GridBagConstraints gbc_typeTextField = new GridBagConstraints();
        gbc_typeTextField.insets = new Insets(0, 0, 5, 0);
        gbc_typeTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_typeTextField.gridx = 1;
        gbc_typeTextField.gridy = 0;
        panel.add(typeTextField, gbc_typeTextField);
        typeTextField.setColumns(10);

        JLabel usedLabel = new JLabel("Used:");
        usedLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_usedLabel = new GridBagConstraints();
        gbc_usedLabel.anchor = GridBagConstraints.EAST;
        gbc_usedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_usedLabel.gridx = 0;
        gbc_usedLabel.gridy = 1;
        panel.add(usedLabel, gbc_usedLabel);

        usedTextField = new JTextField();
        usedTextField.setEditable(false);
        GridBagConstraints gbc_usedTextField = new GridBagConstraints();
        gbc_usedTextField.insets = new Insets(0, 0, 5, 0);
        gbc_usedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_usedTextField.gridx = 1;
        gbc_usedTextField.gridy = 1;
        panel.add(usedTextField, gbc_usedTextField);
        usedTextField.setColumns(10);

        JLabel placedLabel = new JLabel("Placed:");
        placedLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_placedLabel = new GridBagConstraints();
        gbc_placedLabel.anchor = GridBagConstraints.EAST;
        gbc_placedLabel.insets = new Insets(0, 0, 5, 5);
        gbc_placedLabel.gridx = 0;
        gbc_placedLabel.gridy = 2;
        panel.add(placedLabel, gbc_placedLabel);

        placedTextField = new JTextField();
        placedTextField.setEditable(false);
        GridBagConstraints gbc_placedTextField = new GridBagConstraints();
        gbc_placedTextField.insets = new Insets(0, 0, 5, 0);
        gbc_placedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_placedTextField.gridx = 1;
        gbc_placedTextField.gridy = 2;
        panel.add(placedTextField, gbc_placedTextField);
        placedTextField.setColumns(10);

        contentsLabel = new JLabel("Contents:");
        contentsLabel.setVerticalAlignment(SwingConstants.TOP);
        contentsLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_lblContents = new GridBagConstraints();
        gbc_lblContents.fill = GridBagConstraints.VERTICAL;
        gbc_lblContents.insets = new Insets(0, 0, 0, 5);
        gbc_lblContents.gridx = 0;
        gbc_lblContents.gridy = 3;
        panel.add(contentsLabel, gbc_lblContents);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 3;
        panel.add(scrollPane, gbc_scrollPane);

        contentsList = new JList<>();
        scrollPane.setViewportView(contentsList);

        imageLabel = new JLabel("");
        imageLabel.setMinimumSize(new Dimension(250, 0));
        imageLabel.setMaximumSize(new Dimension(250, 0));
        imageLabel.setPreferredSize(new Dimension(250, 0));
        add(imageLabel, BorderLayout.EAST);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
    }
}
