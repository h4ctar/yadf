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
import userinterface.game.WorldPanel;
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

    /** The info panel. */
    private JPanel infoPanel;

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
    public void setDwarf(final IGameCharacter dwarfTmp, final WorldPanel worldCanvas) {
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
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridwidth = 3;
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 9;
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

        infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        add(infoPanel, BorderLayout.CENTER);
        GridBagLayout infoPanelConstraints = new GridBagLayout();
        infoPanelConstraints.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        infoPanelConstraints.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
        infoPanelConstraints.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        infoPanelConstraints.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
        infoPanel.setLayout(infoPanelConstraints);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.anchor = GridBagConstraints.EAST;
        nameLabelConstraints.gridy = 0;
        nameLabelConstraints.gridx = 0;
        infoPanel.add(nameLabel, nameLabelConstraints);
        nameLabel.setForeground(Color.WHITE);

        nameTextField = new JTextField();
        GridBagConstraints nameTextFieldConstraints = new GridBagConstraints();
        nameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameTextFieldConstraints.gridy = 0;
        nameTextFieldConstraints.gridx = 1;
        infoPanel.add(nameTextField, nameTextFieldConstraints);
        nameTextField.setEditable(false);
        nameTextField.setColumns(10);

        JLabel thirstLabel = new JLabel("Thirst:");
        thirstLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints thirstLabelConstraints = new GridBagConstraints();
        thirstLabelConstraints.anchor = GridBagConstraints.EAST;
        thirstLabelConstraints.gridy = 0;
        thirstLabelConstraints.gridx = 2;
        infoPanel.add(thirstLabel, thirstLabelConstraints);
        thirstLabel.setForeground(Color.WHITE);

        thirstTextField = new JTextField();
        GridBagConstraints thirstTextFieldConstraints = new GridBagConstraints();
        thirstTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        thirstTextFieldConstraints.gridy = 0;
        thirstTextFieldConstraints.gridx = 3;
        infoPanel.add(thirstTextField, thirstTextFieldConstraints);
        thirstTextField.setEditable(false);
        thirstTextField.setColumns(10);

        JLabel professionLabel = new JLabel("Profession:");
        professionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints professionLabelConstraints = new GridBagConstraints();
        professionLabelConstraints.anchor = GridBagConstraints.EAST;
        professionLabelConstraints.gridy = 1;
        professionLabelConstraints.gridx = 0;
        infoPanel.add(professionLabel, professionLabelConstraints);
        professionLabel.setForeground(Color.WHITE);

        professionTextField = new JTextField();
        GridBagConstraints professionTextFieldConstraints = new GridBagConstraints();
        professionTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        professionTextFieldConstraints.gridy = 1;
        professionTextFieldConstraints.gridx = 1;
        infoPanel.add(professionTextField, professionTextFieldConstraints);
        professionTextField.setEditable(false);
        professionTextField.setColumns(10);

        JLabel hungerLabel = new JLabel("Hunger:");
        hungerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints hungerLabelConstraints = new GridBagConstraints();
        hungerLabelConstraints.anchor = GridBagConstraints.EAST;
        hungerLabelConstraints.gridy = 1;
        hungerLabelConstraints.gridx = 2;
        infoPanel.add(hungerLabel, hungerLabelConstraints);
        hungerLabel.setForeground(Color.WHITE);

        hungerTextField = new JTextField();
        GridBagConstraints hungerTextFieldConstraints = new GridBagConstraints();
        hungerTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        hungerTextFieldConstraints.gridy = 1;
        hungerTextFieldConstraints.gridx = 3;
        infoPanel.add(hungerTextField, hungerTextFieldConstraints);
        hungerTextField.setEditable(false);
        hungerTextField.setColumns(10);

        JLabel deadLabel = new JLabel("Dead:");
        deadLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints deadLabelConstraints = new GridBagConstraints();
        deadLabelConstraints.anchor = GridBagConstraints.EAST;
        deadLabelConstraints.gridy = 2;
        deadLabelConstraints.gridx = 0;
        infoPanel.add(deadLabel, deadLabelConstraints);
        deadLabel.setForeground(Color.WHITE);

        deadTextField = new JTextField();
        GridBagConstraints deadTextFieldConstraints = new GridBagConstraints();
        deadTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        deadTextFieldConstraints.gridy = 2;
        deadTextFieldConstraints.gridx = 1;
        infoPanel.add(deadTextField, deadTextFieldConstraints);
        deadTextField.setEditable(false);
        deadTextField.setColumns(10);

        lockLabel = new JLabel("Lock:");
        lockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints lockLabelConstraints = new GridBagConstraints();
        lockLabelConstraints.anchor = GridBagConstraints.EAST;
        lockLabelConstraints.gridy = 2;
        lockLabelConstraints.gridx = 2;
        infoPanel.add(lockLabel, lockLabelConstraints);
        lockLabel.setForeground(Color.WHITE);

        lockTextField = new JTextField();
        GridBagConstraints lockTextFieldConstraints = new GridBagConstraints();
        lockTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        lockTextFieldConstraints.gridy = 2;
        lockTextFieldConstraints.gridx = 3;
        infoPanel.add(lockTextField, lockTextFieldConstraints);

        lockTextField.setEditable(false);
        lockTextField.setColumns(10);

        JLabel itemHaulingLabel = new JLabel("Item Hauling:");
        itemHaulingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints itemHaulingLabelConstraints = new GridBagConstraints();
        itemHaulingLabelConstraints.anchor = GridBagConstraints.EAST;
        itemHaulingLabelConstraints.gridy = 3;
        itemHaulingLabelConstraints.gridx = 0;
        infoPanel.add(itemHaulingLabel, itemHaulingLabelConstraints);
        itemHaulingLabel.setForeground(Color.WHITE);

        itemHaulingTextField = new JTextField();
        GridBagConstraints itemHaulingTextFieldConstraints = new GridBagConstraints();
        itemHaulingTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        itemHaulingTextFieldConstraints.gridy = 3;
        itemHaulingTextFieldConstraints.gridx = 1;
        infoPanel.add(itemHaulingTextField, itemHaulingTextFieldConstraints);
        itemHaulingTextField.setEditable(false);
        itemHaulingTextField.setColumns(10);

        JLabel toolHoldingLabel = new JLabel("Tool holding:");
        toolHoldingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints toolHoldingLabelConstraints = new GridBagConstraints();
        toolHoldingLabelConstraints.anchor = GridBagConstraints.EAST;
        toolHoldingLabelConstraints.gridy = 4;
        toolHoldingLabelConstraints.gridx = 0;
        infoPanel.add(toolHoldingLabel, toolHoldingLabelConstraints);
        toolHoldingLabel.setForeground(Color.WHITE);

        toolHoldingTextField = new JTextField();
        GridBagConstraints toolHoldingTextFieldConstraints = new GridBagConstraints();
        toolHoldingTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        toolHoldingTextFieldConstraints.gridy = 4;
        toolHoldingTextFieldConstraints.gridx = 1;
        infoPanel.add(toolHoldingTextField, toolHoldingTextFieldConstraints);
        toolHoldingTextField.setEditable(false);
        toolHoldingTextField.setColumns(10);

        JLabel healthLabel = new JLabel("Health:");
        healthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints healthLabelConstraints = new GridBagConstraints();
        healthLabelConstraints.anchor = GridBagConstraints.EAST;
        healthLabelConstraints.gridy = 5;
        healthLabelConstraints.gridx = 0;
        infoPanel.add(healthLabel, healthLabelConstraints);
        healthLabel.setForeground(Color.WHITE);

        healthTextField = new JTextField();
        GridBagConstraints healthTextFieldConstraints = new GridBagConstraints();
        healthTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        healthTextFieldConstraints.gridy = 5;
        healthTextFieldConstraints.gridx = 1;
        infoPanel.add(healthTextField, healthTextFieldConstraints);
        healthTextField.setEditable(false);
        healthTextField.setColumns(10);

        imageLabel = new JLabel("");
        imageLabel.setPreferredSize(new Dimension(IMAGE_SIZE, 0));
        add(imageLabel, BorderLayout.EAST);
    }
}
