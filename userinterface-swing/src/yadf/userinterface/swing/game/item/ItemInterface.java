package yadf.userinterface.swing.game.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import yadf.simulation.IGameObject;
import yadf.simulation.item.ContainerItem;
import yadf.simulation.item.Item;
import yadf.userinterface.swing.game.AbstractGameObjectInterface;
import yadf.userinterface.swing.misc.SpriteManager;

/**
 * Interface for an item.
 */
public class ItemInterface extends AbstractGameObjectInterface {

    /** The serial version UID. */
    private static final long serialVersionUID = -5779376341490166019L;

    /** The size in pixels of the item image. */
    private static final int IMAGE_SIZE = 200;

    /** The item. */
    private Item item;

    /** The type text field. */
    private JTextField typeTextField;

    /** The used text field. */
    private JTextField usedTextField;

    /** The placed text field. */
    private JTextField placedTextField;

    /** The panel text field. */
    protected JPanel infoPanel;

    /** The contents label. */
    private JLabel contentsLabel;

    /** The contents list. */
    private JList<Item> contentsList = new JList<>();

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

    @Override
    protected void setup(final IGameObject gameObject) {
        item = (Item) gameObject;
        typeTextField.setText(item.getType().toString());
        usedTextField.setText(Boolean.toString(item.isUsed()));
        placedTextField.setText(Boolean.toString(item.isPlaced()));
        if (item instanceof ContainerItem) {
            contentsLabel.setVisible(true);
            scrollPane.setVisible(true);
            contentsList.setListData(((ContainerItem) item).getItems().toArray(new Item[0]));
        } else {
            contentsLabel.setVisible(false);
            scrollPane.setVisible(false);
        }
        Image itemImage = SpriteManager.getInstance().getItemSprite(item.getType().sprite).getImage();
        itemImage = itemImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST);
        imageLabel.setIcon(new ImageIcon(itemImage));
    }

    @Override
    public String getTitle() {
        return item.getType().name;
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));

        infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        GridBagLayout infoPanelConstraints = new GridBagLayout();
        infoPanelConstraints.columnWidths = new int[] { 0, 0, 0 };
        infoPanelConstraints.columnWeights = new double[] { 0.0, 0.0, 1.0, };
        infoPanelConstraints.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        infoPanelConstraints.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
        infoPanel.setLayout(infoPanelConstraints);
        add(infoPanel);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(Color.WHITE);
        GridBagConstraints typeLabelConstraints = new GridBagConstraints();
        typeLabelConstraints.anchor = GridBagConstraints.EAST;
        typeLabelConstraints.gridx = 0;
        typeLabelConstraints.gridy = 0;
        infoPanel.add(typeLabel, typeLabelConstraints);

        typeTextField = new JTextField();
        typeTextField.setEditable(false);
        GridBagConstraints typeTextFieldConstraints = new GridBagConstraints();
        typeTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        typeTextFieldConstraints.gridx = 1;
        typeTextFieldConstraints.gridy = 0;
        infoPanel.add(typeTextField, typeTextFieldConstraints);
        typeTextField.setColumns(10);

        JLabel usedLabel = new JLabel("Used:");
        usedLabel.setForeground(Color.WHITE);
        GridBagConstraints usedLabelConstraints = new GridBagConstraints();
        usedLabelConstraints.anchor = GridBagConstraints.EAST;
        usedLabelConstraints.gridx = 0;
        usedLabelConstraints.gridy = 1;
        infoPanel.add(usedLabel, usedLabelConstraints);

        usedTextField = new JTextField();
        usedTextField.setEditable(false);
        GridBagConstraints usedTextFieldConstraints = new GridBagConstraints();
        usedTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        usedTextFieldConstraints.gridx = 1;
        usedTextFieldConstraints.gridy = 1;
        infoPanel.add(usedTextField, usedTextFieldConstraints);
        usedTextField.setColumns(10);

        JLabel placedLabel = new JLabel("Placed:");
        placedLabel.setForeground(Color.WHITE);
        GridBagConstraints placedLabelConstraints = new GridBagConstraints();
        placedLabelConstraints.anchor = GridBagConstraints.EAST;
        placedLabelConstraints.gridx = 0;
        placedLabelConstraints.gridy = 2;
        infoPanel.add(placedLabel, placedLabelConstraints);

        placedTextField = new JTextField();
        placedTextField.setEditable(false);
        GridBagConstraints placedTextFieldConstraints = new GridBagConstraints();
        placedTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        placedTextFieldConstraints.gridx = 1;
        placedTextFieldConstraints.gridy = 2;
        infoPanel.add(placedTextField, placedTextFieldConstraints);
        placedTextField.setColumns(10);
        placedLabelConstraints.anchor = GridBagConstraints.EAST;

        contentsLabel = new JLabel("Contents:");
        contentsLabel.setVerticalAlignment(SwingConstants.TOP);
        contentsLabel.setForeground(Color.WHITE);
        GridBagConstraints contentsLabelConstraints = new GridBagConstraints();
        contentsLabelConstraints.gridx = 0;
        contentsLabelConstraints.gridy = 3;
        infoPanel.add(contentsLabel, contentsLabelConstraints);

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(200, 0));
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.gridheight = 2;
        scrollPaneConstraints.gridx = 1;
        scrollPaneConstraints.gridy = 3;
        scrollPane.setViewportView(contentsList);
        infoPanel.add(scrollPane, scrollPaneConstraints);

        imageLabel = new JLabel("");
        imageLabel.setPreferredSize(new Dimension(IMAGE_SIZE, 0));
        add(imageLabel, BorderLayout.EAST);
    }
}
