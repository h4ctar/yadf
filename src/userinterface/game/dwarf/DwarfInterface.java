/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package userinterface.game.dwarf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import simulation.character.ICharacterListener;
import simulation.character.IGameCharacter;
import simulation.character.component.ICharacterComponentListener;
import simulation.character.component.IEatDrinkComponent;
import simulation.character.component.IHealthComponent;
import simulation.character.component.IInventoryComponent;
import simulation.character.component.ISkillComponent;
import simulation.item.Item;
import simulation.labor.LaborType;
import userinterface.game.WorldCanvas;
import userinterface.misc.SpriteManager;

/**
 * The Class DwarfInterface.
 */
public class DwarfInterface extends JPanel implements ICharacterListener, ICharacterComponentListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 6213975713592520095L;

    /** The size in pixels of the dwarf image. */
    private static final int IMAGE_SIZE = 200;

    /** The name text field. */
    private JTextField nameTextField;

    /** The profession text field. */
    private JTextField professionTextField;

    /** The alive text field. */
    private JTextField deadTextField;

    /** The item hauling text field. */
    private JTextField itemHaulingTextField;

    /** The tool holding text field. */
    private JTextField toolHoldingTextField;

    /** The health text field. */
    private JTextField healthTextField;

    /** The hunger text field. */
    private JTextField hungerTextField;

    /** The thirst text field. */
    private JTextField thirstTextField;

    /** The dwarf. */
    private IGameCharacter dwarf;

    /** The lbl lock. */
    private JLabel lockLabel;

    /** The lock text field. */
    private JTextField lockTextField;

    /** The lbl new label. */
    private JLabel imageLabel;

    /** The button panel. */
    private JPanel buttonPanel;

    /** The zoom button. */
    private JButton zoomButton;

    /** The follow button. */
    private JButton followButton;
    private JPanel panel;

    /**
     * Create the frame.
     */
    public DwarfInterface() {
        setBackground(Color.BLACK);
        setOpaque(false);
        setupLayout();
    }

    @Override
    public void characterChanged(final Object character) {
        assert character == dwarf;
        update();
    }

    @Override
    public void componentChanged(final Object component) {
        update();
    }

    /**
     * Sets the dwarf.
     * @param dwarfTmp the dwarf
     * @param worldCanvas the world canvas
     */
    public void setDwarf(final IGameCharacter dwarfTmp, final WorldCanvas worldCanvas) {
        dwarf = dwarfTmp;
        dwarf.addListener(this);
        dwarf.getComponent(ISkillComponent.class).addListener(this);
        dwarf.getComponent(IHealthComponent.class).addListener(this);
        dwarf.getComponent(IInventoryComponent.class).addListener(this);
        dwarf.getComponent(IEatDrinkComponent.class).addListener(this);
        update();
    }

    /**
     * Update.
     */
    public void update() {
        nameTextField.setText(dwarf.getName());
        LaborType profession = dwarf.getComponent(ISkillComponent.class).getProfession();
        professionTextField.setText(profession.professionName);
        deadTextField.setText(Boolean.toString(dwarf.isDead()));
        Item haulItem = dwarf.getComponent(IInventoryComponent.class).getHaulItem();
        itemHaulingTextField.setText(haulItem == null ? "Not hauling" : haulItem.getType().name);
        Item tool = dwarf.getComponent(IInventoryComponent.class).getToolHolding();
        toolHoldingTextField.setText(tool == null ? "No tool" : tool.getType().name);
        healthTextField.setText(Integer.toString(dwarf.getComponent(IHealthComponent.class).getHealth()));
        hungerTextField.setText(Integer.toString(dwarf.getComponent(IEatDrinkComponent.class).getHunger()));
        thirstTextField.setText(Integer.toString(dwarf.getComponent(IEatDrinkComponent.class).getThirst()));
        lockTextField.setText(Boolean.toString(dwarf.isLocked()));
        Image dwarfImage = SpriteManager.getInstance().getItemSprite(profession.sprite).getImage();
        dwarfImage = dwarfImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST);
        imageLabel.setIcon(new ImageIcon(dwarfImage));
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
        gbc_buttonPanel.gridwidth = 3;
        gbc_buttonPanel.insets = new Insets(0, 0, 0, 5);
        gbc_buttonPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_buttonPanel.gridx = 0;
        gbc_buttonPanel.gridy = 9;
        add(buttonPanel, BorderLayout.WEST);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        zoomButton = new JButton("Zoom");
        zoomButton.setPreferredSize(new Dimension(150, 23));
        zoomButton.setMinimumSize(new Dimension(150, 23));
        zoomButton.setMaximumSize(new Dimension(150, 23));
        zoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(zoomButton);

        followButton = new JButton("Follow");
        followButton.setPreferredSize(new Dimension(150, 23));
        followButton.setMinimumSize(new Dimension(150, 23));
        followButton.setMaximumSize(new Dimension(150, 23));
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(followButton);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setOpaque(false);
        add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
        panel.setLayout(gbl_panel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_nameLabel = new GridBagConstraints();
        gbc_nameLabel.anchor = GridBagConstraints.EAST;
        gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_nameLabel.gridy = 0;
        gbc_nameLabel.gridx = 0;
        panel.add(nameLabel, gbc_nameLabel);
        nameLabel.setForeground(Color.WHITE);

        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(150, 20));
        nameTextField.setMinimumSize(new Dimension(150, 20));
        nameTextField.setMaximumSize(new Dimension(150, 20));
        GridBagConstraints gbc_nameTextField = new GridBagConstraints();
        gbc_nameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_nameTextField.gridy = 0;
        gbc_nameTextField.gridx = 1;
        panel.add(nameTextField, gbc_nameTextField);
        nameTextField.setEditable(false);
        nameTextField.setColumns(10);

        JLabel professionLabel = new JLabel("Profession:");
        professionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_professionLabel = new GridBagConstraints();
        gbc_professionLabel.anchor = GridBagConstraints.EAST;
        gbc_professionLabel.insets = new Insets(0, 0, 5, 5);
        gbc_professionLabel.gridy = 1;
        gbc_professionLabel.gridx = 0;
        panel.add(professionLabel, gbc_professionLabel);
        professionLabel.setForeground(Color.WHITE);

        professionTextField = new JTextField();
        GridBagConstraints gbc_professionTextField = new GridBagConstraints();
        gbc_professionTextField.insets = new Insets(0, 0, 5, 5);
        gbc_professionTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_professionTextField.gridy = 1;
        gbc_professionTextField.gridx = 1;
        panel.add(professionTextField, gbc_professionTextField);
        professionTextField.setEditable(false);
        professionTextField.setColumns(10);

        JLabel deadLabel = new JLabel("Dead:");
        deadLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_deadLabel = new GridBagConstraints();
        gbc_deadLabel.anchor = GridBagConstraints.EAST;
        gbc_deadLabel.insets = new Insets(0, 0, 5, 5);
        gbc_deadLabel.gridy = 2;
        gbc_deadLabel.gridx = 0;
        panel.add(deadLabel, gbc_deadLabel);
        deadLabel.setForeground(Color.WHITE);

        deadTextField = new JTextField();
        GridBagConstraints gbc_deadTextField = new GridBagConstraints();
        gbc_deadTextField.insets = new Insets(0, 0, 5, 5);
        gbc_deadTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_deadTextField.gridy = 2;
        gbc_deadTextField.gridx = 1;
        panel.add(deadTextField, gbc_deadTextField);
        deadTextField.setEditable(false);
        deadTextField.setColumns(10);

        JLabel itemHaulingLabel = new JLabel("Item Hauling:");
        itemHaulingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_itemHaulingLabel = new GridBagConstraints();
        gbc_itemHaulingLabel.anchor = GridBagConstraints.EAST;
        gbc_itemHaulingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_itemHaulingLabel.gridy = 3;
        gbc_itemHaulingLabel.gridx = 0;
        panel.add(itemHaulingLabel, gbc_itemHaulingLabel);
        itemHaulingLabel.setForeground(Color.WHITE);

        itemHaulingTextField = new JTextField();
        GridBagConstraints gbc_itemHaulingTextField = new GridBagConstraints();
        gbc_itemHaulingTextField.insets = new Insets(0, 0, 5, 5);
        gbc_itemHaulingTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_itemHaulingTextField.gridy = 3;
        gbc_itemHaulingTextField.gridx = 1;
        panel.add(itemHaulingTextField, gbc_itemHaulingTextField);
        itemHaulingTextField.setEditable(false);
        itemHaulingTextField.setColumns(10);

        JLabel toolHoldingLabel = new JLabel("Tool holding:");
        toolHoldingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_toolHoldingLabel = new GridBagConstraints();
        gbc_toolHoldingLabel.anchor = GridBagConstraints.EAST;
        gbc_toolHoldingLabel.insets = new Insets(0, 0, 5, 5);
        gbc_toolHoldingLabel.gridy = 4;
        gbc_toolHoldingLabel.gridx = 0;
        panel.add(toolHoldingLabel, gbc_toolHoldingLabel);
        toolHoldingLabel.setForeground(Color.WHITE);

        toolHoldingTextField = new JTextField();
        GridBagConstraints gbc_toolHoldingTextField = new GridBagConstraints();
        gbc_toolHoldingTextField.insets = new Insets(0, 0, 5, 5);
        gbc_toolHoldingTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolHoldingTextField.gridy = 4;
        gbc_toolHoldingTextField.gridx = 1;
        panel.add(toolHoldingTextField, gbc_toolHoldingTextField);
        toolHoldingTextField.setEditable(false);
        toolHoldingTextField.setColumns(10);

        JLabel healthLabel = new JLabel("Health:");
        healthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_healthLabel = new GridBagConstraints();
        gbc_healthLabel.anchor = GridBagConstraints.EAST;
        gbc_healthLabel.insets = new Insets(0, 0, 5, 5);
        gbc_healthLabel.gridy = 5;
        gbc_healthLabel.gridx = 0;
        panel.add(healthLabel, gbc_healthLabel);
        healthLabel.setForeground(Color.WHITE);

        healthTextField = new JTextField();
        GridBagConstraints gbc_healthTextField = new GridBagConstraints();
        gbc_healthTextField.insets = new Insets(0, 0, 5, 5);
        gbc_healthTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_healthTextField.gridy = 5;
        gbc_healthTextField.gridx = 1;
        panel.add(healthTextField, gbc_healthTextField);
        healthTextField.setEditable(false);
        healthTextField.setColumns(10);

        JLabel hungerLabel = new JLabel("Hunger:");
        hungerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_hungerLabel = new GridBagConstraints();
        gbc_hungerLabel.anchor = GridBagConstraints.EAST;
        gbc_hungerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_hungerLabel.gridy = 6;
        gbc_hungerLabel.gridx = 0;
        panel.add(hungerLabel, gbc_hungerLabel);
        hungerLabel.setForeground(Color.WHITE);

        hungerTextField = new JTextField();
        GridBagConstraints gbc_hungerTextField = new GridBagConstraints();
        gbc_hungerTextField.insets = new Insets(0, 0, 5, 5);
        gbc_hungerTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_hungerTextField.gridy = 6;
        gbc_hungerTextField.gridx = 1;
        panel.add(hungerTextField, gbc_hungerTextField);
        hungerTextField.setEditable(false);
        hungerTextField.setColumns(10);

        JLabel thirstLabel = new JLabel("Thirst:");
        thirstLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_thirstLabel = new GridBagConstraints();
        gbc_thirstLabel.anchor = GridBagConstraints.EAST;
        gbc_thirstLabel.insets = new Insets(0, 0, 5, 5);
        gbc_thirstLabel.gridy = 7;
        gbc_thirstLabel.gridx = 0;
        panel.add(thirstLabel, gbc_thirstLabel);
        thirstLabel.setForeground(Color.WHITE);

        thirstTextField = new JTextField();
        GridBagConstraints gbc_thirstTextField = new GridBagConstraints();
        gbc_thirstTextField.insets = new Insets(0, 0, 5, 5);
        gbc_thirstTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_thirstTextField.gridy = 7;
        gbc_thirstTextField.gridx = 1;
        panel.add(thirstTextField, gbc_thirstTextField);
        thirstTextField.setEditable(false);
        thirstTextField.setColumns(10);

        lockLabel = new JLabel("Lock:");
        lockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbc_lockLabel = new GridBagConstraints();
        gbc_lockLabel.anchor = GridBagConstraints.EAST;
        gbc_lockLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lockLabel.gridy = 8;
        gbc_lockLabel.gridx = 0;
        panel.add(lockLabel, gbc_lockLabel);
        lockLabel.setForeground(Color.WHITE);

        lockTextField = new JTextField();
        GridBagConstraints gbc_lockTextField = new GridBagConstraints();
        gbc_lockTextField.insets = new Insets(0, 0, 0, 5);
        gbc_lockTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_lockTextField.gridy = 8;
        gbc_lockTextField.gridx = 1;
        panel.add(lockTextField, gbc_lockTextField);

        lockTextField.setEditable(false);
        lockTextField.setColumns(10);

        imageLabel = new JLabel("");
        imageLabel.setPreferredSize(new Dimension(250, 0));
        imageLabel.setMinimumSize(new Dimension(250, 0));
        imageLabel.setMaximumSize(new Dimension(250, 0));
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        add(imageLabel, BorderLayout.EAST);
    }
}
